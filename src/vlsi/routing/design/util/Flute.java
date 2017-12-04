package vlsi.routing.design.util;


/**
* READ THIS LICENSE AGREEMENT CAREFULLY BEFORE USING THIS PRODUCT. BY USING
* THIS PRODUCT YOU INDICATE YOUR ACCEPTANCE OF THE TERMS OF THE FOLLOWING
* AGREEMENT. THESE TERMS APPLY TO YOU AND ANY SUBSEQUENT LICENSEE OF THIS
* PRODUCT.
* 
* License Agreement for FLUTE
* 
* Copyright (c) 2004 by Dr. Chris C. N. Chu
* All rights reserved
* 
* Copyright (c) 2012 by Stefan M�cke -- Java port
* All rights reserved
* 
* ATTRIBUTION ASSURANCE LICENSE (adapted from the original BSD license)
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the conditions below are
* met. These conditions require a modest attribution to Dr. Chris C. N. Chu
* (the "Author").
* 
* 1. Redistributions of the source code, with or without modification (the
*    "Code"), must be accompanied by any documentation and, each time
*    the resulting executable program or a program dependent thereon is
*    launched, a prominent display (e.g., splash screen or banner text) of
*    the Author's attribution information, which includes:
*    (a) Dr. Chris C. N. Chu ("AUTHOR"),
*    (b) Iowa State University ("PROFESSIONAL IDENTIFICATION"), and
*    (c) http://home.eng.iastate.edu/~cnchu/ ("URL").
* 
* 2. Users who intend to use the Code for commercial purposes will notify
*    Author prior to such commercial use.
* 
* 3. Neither the name nor any trademark of the Author may be used to
*    endorse or promote products derived from this software without
*    specific prior written permission.
* 
* 4. Users are entirely responsible, to the exclusion of the Author and any
*    other persons, for compliance with (1) regulations set by owners or
*    administrators of employed equipment, (2) licensing terms of any other
*    software, and (3) local, national, and international regulations
*    regarding use, including those regarding import, export, and use of
*    encryption software.
* 
* THIS FREE SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
* IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
* OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL THE AUTHOR OR ANY CONTRIBUTOR BE LIABLE FOR ANY DIRECT,
* INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, EFFECTS OF UNAUTHORIZED OR MALICIOUS
* NETWORK ACCESS; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
* THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
* THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
* Implementation of the FLUTE algorithm. This class is a port from the original C code (version 2.5)
* developed by Dr. Chris C. N. Chu.
* <p>
* Note: This implementation is <i>not</i> thread-safe.
* </p>
*/
public class Flute {

	// TODO Use faster sorting algorithm
	// TODO Recycle some more objects (Tree, Branch, empty Point arrays)
	// TODO Trim LUT arrays for lower memory consumption; sub-arrays in Java can be sized differently in contrast to C

	/**
	 * LUT for POWV (Wirelength Vector)
	 */
	public static final String POWVFILE = "POWV9.dat";

	/**
	 * LUT for POST (Steiner Tree)
	 */
	public static final String POSTFILE = "POST9.dat";

	/**
	 * Max. degree of a net that can be handled;
	 * Setting MAXD to more than 150 is not recommended
	 */
	public static final int MAXD = 150;

	/**
	 * LUT is used for d <= D, D <= 9
	 */
	public static final int D = 9;

	/**
	 * true to construct routing, false to estimate WL only
	 */
	public static final boolean ROUTING = true;

	/**
	 * Default accuracy
	 */
	public static final int ACCURACY = 3;

	/**
	 * Remove dup. pin for flute_wl() & flute()
	 */
	public static final boolean REMOVE_DUPLICATE_PIN = false;

	/**
	 * Suggestion: Set to 1 if ACCURACY >= 5
	 */
	public static final boolean LOCAL_REFINEMENT = false;

	public static class Tree {
		/** degree */
		public int deg;
		/** total wirelength */
		public int length;
		/** array of tree branches */
		public Branch[] branch;
	}

	public static class Branch {
		/** starting point of the branch */
		public int x, y;
		/** index of neighbor */
		public int n;
	}

	private static class Point {
		public int x, y;
		public int o;
	}

	private static class Csoln {
		public int parent;
		/** Add: 0..i, Sub: j..10; seg[i+1]=seg[j-1]=0 */
		public int[] seg = new int[11];
		/** row = rowcol[]/16, col = rowcol[]%16 */
		public int[] rowcol = new int[D - 2];
		public int[] neighbor = new int[2 * D - 2];
	}

	private static class Pool<E> extends ArrayList<E> {
		private int last = -1;
		public E get() {
			if (last == -1)
				return null;
			return super.remove(last--);
		}
		public void recycle(E item) {
			super.add(item);
			last++;
		}
	}

	private static final int MGROUP;
	private static final int MPOWV;

	static {
		int d = D;
		if (d <= 7) {
			MGROUP = 5040 / 4; // Max. # of groups, 7! = 5040
			MPOWV = 15; // Max. # of POWVs per group
		} else if (d == 8) {
			MGROUP = 40320 / 4; // Max. # of groups, 8! = 40320
			MPOWV = 33; // Max. # of POWVs per group
		} else if (d == 9) {
			MGROUP = 362880 / 4; // Max. # of groups, 9! = 362880
			MPOWV = 79; // Max. # of POWVs per group
		} else {
			throw new RuntimeException("D too large: " + D);
		}
	}

	private int[] numgrp = {0, 0, 0, 0, 6, 30, 180, 1260, 10080, 90720};
	private Csoln[][][] LUT = new Csoln[D + 1][MGROUP][]; // storing 4 .. D
	private int[][] numsoln = new int[D + 1][MGROUP];

	// Pool of reusable points
	private Pool<Point[]> pointsPool = new Pool<Point[]>();

	public Flute() {
	}

	public void readLUT(File dataDir) throws IOException {
		File powvFile = new File(dataDir, POWVFILE);
		File postFile = new File(dataDir, POSTFILE);
		readLUT(powvFile, postFile);
	}

	public void readLUT(File powvFile, File postFile) throws IOException {
		InputStream powvStream = new BufferedInputStream(new FileInputStream(powvFile));
		InputStream postStream = null;

		if (ROUTING) {
			postStream = new BufferedInputStream(new FileInputStream(postFile));
		}

		readLUT(powvStream, postStream);
	}

	public void readLUT(InputStream powvStream, InputStream postStream) throws IOException {
		char[] charnum = new char[256];
		char[] lineBuf = new char[32];
		int linep;
		char c;
		int[] number = new int[1];

		for (int i = 0; i <= 255; i++) {
			if ('0' <= i && i <= '9')
				charnum[i] = (char) (i - '0');
			else if (i >= 'A')
				charnum[i] = (char) (i - 'A' + 10);
			else
				// if (i=='$' || i=='\n' || ... )
				charnum[i] = 0;
		}

		for (int d = 4; d <= D; d++) {
			// d=%d\n
			linep = readLine(powvStream, lineBuf);
			linep = scanString(lineBuf, linep, "d=");
			linep = scanNumber(lineBuf, linep, number);
			d = number[0];
			scanEOL(lineBuf, linep);

			// d=%d\n
			if (ROUTING) {
				linep = readLine(postStream, lineBuf);
				linep = scanString(lineBuf, linep, "d=");
				linep = scanNumber(lineBuf, linep, number);
				d = number[0];
				scanEOL(lineBuf, linep);
			}

			for (int k = 0; k < numgrp[d]; k++) {
				int ns = charnum[powvStream.read() & 0xff];

				if (ns == 0) { // same as some previous group
					// %d\n
					linep = readLine(powvStream, lineBuf);
					linep = scanNumber(lineBuf, linep, number);
					int kk = number[0];
					scanEOL(lineBuf, linep);
					numsoln[d][k] = numsoln[d][kk];
					LUT[d][k] = LUT[d][kk];
				} else {
					powvStream.read(); // '\n'
					numsoln[d][k] = ns;
					Csoln[] p = new Csoln[ns];
					for (int i = 0; i < ns; i++) {
						p[i] = new Csoln();
					}
					int poffset = 0; // Java workaround for C-style pointer arithmetic on 'p'
					LUT[d][k] = p;
					for (int i = 1; i <= ns; i++) {
						linep = readLine(powvStream, lineBuf);
						p[poffset].parent = charnum[lineBuf[linep++]];
						int j = 0;
						while ((p[poffset].seg[j++] = charnum[lineBuf[linep++]]) != 0) {
						}
						j = 10;
						while ((p[poffset].seg[j--] = charnum[lineBuf[linep++]]) != 0) {
						}
						if (ROUTING) {
							int nn = 2 * d - 2;
							readChars(postStream, lineBuf, d - 2);
							linep = 0;
							for (j = d; j < nn; j++) {
								c = charnum[lineBuf[linep++]];
								p[poffset].rowcol[j - d] = c;
							}
							readChars(postStream, lineBuf, nn / 2 + 1); // last char \n
							linep = 0;
							for (j = 0; j < nn;) {
								c = lineBuf[linep++];
								p[poffset].neighbor[j++] = c / 16;
								p[poffset].neighbor[j++] = c % 16;
							}
						}
						poffset++;
					}
				}
			}
		}

		powvStream.close();
		if (postStream != null) {
			postStream.close();
		}
	}

	private int readLine(InputStream in, char[] buf) throws IOException {
		int c;
		int i = 0;
		while ((c = in.read()) != -1) {
			if (c == '\n') {
				buf[i] = '\n';
				break;
			}
			buf[i++] = (char) (c & 0xff);
		}
		return 0;
	}

	private void readChars(InputStream in, char[] buf, int count) throws IOException {
		for (int i = 0; i < count; i++) {
			buf[i] = (char) (in.read() & 0xff);
		}
	}

	private int scanNumber(char[] buf, int offset, int[] result) throws IOException {
		int c = buf[offset++];
		if (!isDigit(c))
			throw new RuntimeException("Reading error. Expected digit but got '" + c + "'.");
		int number = c - '0';
		while (isDigit(c = buf[offset++])) {
			number = number * 10 + (c - '0');
		}
		result[0] = number;
		return offset - 1;
	}

	private int scanString(char[] buf, int offset, String str) throws IOException {
		int len = str.length();
		for (int i = 0; i < len; i++) {
			int c = buf[offset + i];
			if (c != str.charAt(i))
				throw new RuntimeException("Reading error. Expected '"
						+ str.charAt(i)
						+ "' but got '"
						+ c
						+ "'.");
		}
		return offset + len;
	}

	private void scanEOL(char[] buf, int offset) throws IOException {
		if (buf[offset] != '\n')
			throw new RuntimeException("Reading error. Expected EOL but got '" + buf[offset] + "'.");
	}

	private boolean isDigit(int c) {
		return '0' <= c && c <= '9';
	}

	public int flute_wl(int[] x, int[] y, int acc) {
		return flute_wl(x.length, x, y, acc);
	}

	public int flute_wl(int d, int[] x, int[] y, int acc) {
		int l;

		if (d == 2)
			l = ADIFF(x[0], x[1]) + ADIFF(y[0], y[1]);
		else if (d == 3) {
			int xu, xl, yu, yl;
			if (x[0] > x[1]) {
				xu = max(x[0], x[2]);
				xl = min(x[1], x[2]);
			} else {
				xu = max(x[1], x[2]);
				xl = min(x[0], x[2]);
			}
			if (y[0] > y[1]) {
				yu = max(y[0], y[2]);
				yl = min(y[1], y[2]);
			} else {
				yu = max(y[1], y[2]);
				yl = min(y[0], y[2]);
			}
			l = (xu - xl) + (yu - yl);
		} else {
			final Point[] pt = newPointsMAXD();
			final Point[] ptp = new Point[MAXD];
			Point tmpp;
			for (int i = 0; i < d; i++) {
				pt[i].x = x[i];
				pt[i].y = y[i];
				ptp[i] = pt[i];
			}

			// sort x
			for (int i = 0; i < d - 1; i++) {
				int minval = ptp[i].x;
				int minidx = i;
				for (int j = i + 1; j < d; j++) {
					if (minval > ptp[j].x) {
						minval = ptp[j].x;
						minidx = j;
					}
				}
				tmpp = ptp[i];
				ptp[i] = ptp[minidx];
				ptp[minidx] = tmpp;
			}

			if (REMOVE_DUPLICATE_PIN) {
				ptp[d] = pt[d];
				ptp[d].x = ptp[d].y = -999999;
				int j = 0;
				for (int i = 0; i < d; i++) {
					int k;
					for (k = i + 1; ptp[k].x == ptp[i].x; k++) {
						if (ptp[k].y == ptp[i].y) // pins k and i are the same
							break;
					}
					if (ptp[k].x != ptp[i].x) {
						ptp[j++] = ptp[i];
					}
				}
				d = j;
			}

			int[] xs = new int[MAXD];
			int[] ys = new int[MAXD];
			for (int i = 0; i < d; i++) {
				xs[i] = ptp[i].x;
				ptp[i].o = i;
			}

			// sort y to find s[]
			int[] s = new int[MAXD];
			for (int i = 0; i < d - 1; i++) {
				int minval = ptp[i].y;
				int minidx = i;
				for (int j = i + 1; j < d; j++) {
					if (minval > ptp[j].y) {
						minval = ptp[j].y;
						minidx = j;
					}
				}
				ys[i] = ptp[minidx].y;
				s[i] = ptp[minidx].o;
				ptp[minidx] = ptp[i];
			}
			ys[d - 1] = ptp[d - 1].y;
			s[d - 1] = ptp[d - 1].o;

			l = flutes_wl(d, xs, ys, s, acc);
			pointsPool.recycle(pt);
		}
		return l;
	}

	public int flutes_wl(int d, int xs[], int ys[], int s[], int acc) {
		if (REMOVE_DUPLICATE_PIN) {
			return flutes_wl_RDP(d, xs, ys, s, acc);
		} else {
			return flutes_wl_ALLD(d, xs, ys, s, acc);
		}
	}

	// xs[] and ys[] are coords in x and y in sorted order
	// s[] is a list of nodes in increasing y direction
	//   if nodes are indexed in the order of increasing x coord
	//   i.e., s[i] = s_i as defined in paper
	// The points are (xs[s[i]], ys[i]) for i=0..d-1
	//             or (xs[i], ys[si[i]]) for i=0..d-1

	private int flutes_wl_RDP(int d, int xs[], int ys[], int s[], int acc) {
		for (int i = 0; i < d - 1; i++) {
			if (xs[s[i]] == xs[s[i + 1]] && ys[i] == ys[i + 1]) {
				int ss;
				if (s[i] < s[i + 1])
					ss = s[i + 1];
				else {
					ss = s[i];
					s[i] = s[i + 1];
				}
				for (int j = i + 2; j < d; j++) {
					ys[j - 1] = ys[j];
					s[j - 1] = s[j];
				}
				for (int j = ss + 1; j < d; j++)
					xs[j - 1] = xs[j];
				for (int j = 0; j <= d - 2; j++)
					if (s[j] > ss)
						s[j]--;
				i--;
				d--;
			}
		}
		return flutes_wl_ALLD(d, xs, ys, s, acc);
	}

	// For low-degree, i.e., 2 <= d <= D
	private int flutes_wl_LD(int d, int[] xs, int xoffs, int[] ys, int yoffs, int[] s) {
		int minl;

		if (d <= 3)
			minl = xs[d - 1 + xoffs] - xs[xoffs] + ys[d - 1 + yoffs] - ys[yoffs];
		else {
			int k = 0;
			if (s[0] < s[2])
				k++;
			if (s[1] < s[2])
				k++;

			for (int i = 3; i <= d - 1; i++) { // p0=0 always, skip i=1 for symmetry
				int pi = s[i];
				for (int j = d - 1; j > i; j--)
					if (s[j] < s[i])
						pi--;
				k = pi + (i + 1) * k;
			}

			int[] dd = new int[2 * D - 2]; // 0..D-2 for v, D-1..2*D-3 for h
			if (k < numgrp[d]) // no horizontal flip
				for (int i = 1; i <= d - 3; i++) {
					dd[i] = ys[i + 1 + yoffs] - ys[i + yoffs];
					dd[d - 1 + i] = xs[i + 1 + xoffs] - xs[i + xoffs];
				}
			else {
				k = 2 * numgrp[d] - 1 - k;
				for (int i = 1; i <= d - 3; i++) {
					dd[i] = ys[i + 1 + yoffs] - ys[i + yoffs];
					dd[d - 1 + i] = xs[d - 1 - i + xoffs] - xs[d - 2 - i + xoffs];
				}
			}

			int[] l = new int[MPOWV + 1];
			minl = l[0] = xs[d - 1 + xoffs] - xs[xoffs] + ys[d - 1 + yoffs] - ys[yoffs];
			Csoln[] rlist = LUT[d][k];
			Csoln r = rlist[0];
			for (int i = 0; r.seg[i] > 0; i++)
				minl += dd[r.seg[i]];

			l[1] = minl;
			int j = 2;
			int rlistOffset = 0;
			int sum = 0;
			while (j <= numsoln[d][k]) {
				rlistOffset++;
				r = rlist[rlistOffset];
				sum = l[r.parent];
				for (int i = 0; r.seg[i] > 0; i++)
					sum += dd[r.seg[i]];
				for (int i = 10; r.seg[i] > 0; i--)
					sum -= dd[r.seg[i]];
				minl = min(minl, sum);
				l[j++] = sum;
			}
		}

		return minl;
	}

	// For medium-degree, i.e., D+1 <= d
	private int flutes_wl_MD(int d, int xs[], int xoffs, int ys[], int yoffs, int s[], int acc) {
		int[] x1 = new int[MAXD];
		int[] y1 = new int[MAXD];
		int[] si = new int[MAXD];
		int[] s1 = new int[MAXD];
		int[] s2 = new int[MAXD];
		float[] score = new float[2 * MAXD], penalty = new float[MAXD];

		if (s[0] < s[d - 1]) {
			int ms = max(s[0], s[1]);
			for (int i = 2; i <= ms; i++)
				ms = max(ms, s[i]);
			if (ms <= d - 3) {
				for (int i = 0; i <= ms; i++) {
					x1[i] = xs[i + xoffs];
					y1[i] = ys[i + yoffs];
					s1[i] = s[i];
				}
				x1[ms + 1] = xs[ms + xoffs];
				y1[ms + 1] = ys[ms + yoffs];
				s1[ms + 1] = ms + 1;

				s2[0] = 0;
				for (int i = 1; i <= d - 1 - ms; i++)
					s2[i] = s[i + ms] - ms;

				return flutes_wl_LMD(ms + 2, x1, y1, s1, acc)
						+ flutes_wl_LMD(d - ms, xs, ms + xoffs, ys, ms + yoffs, s2, acc);
			}
		} else { // (s[0] > s[d-1])
			int ms = min(s[0], s[1]);
			for (int i = 2; i <= d - 1 - ms; i++)
				ms = min(ms, s[i]);
			if (ms >= 2) {
				x1[0] = xs[ms + xoffs];
				y1[0] = ys[0 + yoffs];
				s1[0] = s[0] - ms + 1;
				for (int i = 1; i <= d - 1 - ms; i++) {
					x1[i] = xs[i + ms - 1 + xoffs];
					y1[i] = ys[i + yoffs];
					s1[i] = s[i] - ms + 1;
				}
				x1[d - ms] = xs[d - 1 + xoffs];
				y1[d - ms] = ys[d - 1 - ms + yoffs];
				s1[d - ms] = 0;

				s2[0] = ms;
				for (int i = 1; i <= ms; i++)
					s2[i] = s[i + d - 1 - ms];

				return flutes_wl_LMD(d + 1 - ms, x1, y1, s1, acc)
						+ flutes_wl_LMD(ms + 1, xs, xoffs, ys, d - 1 - ms + yoffs, s2, acc);
			}
		}

		// Find inverse si[] of s[]
		for (int r = 0; r < d; r++)
			si[s[r]] = r;

		// Determine breaking directions and positions dp[]
		int lb = (d - 2 * acc + 2) / 4;
		if (lb < 2)
			lb = 2;
		int ub = d - 1 - lb;

		// Compute scores    
		final float AAWL = 0.6f;
		final float BBWL = 0.3f;
		float CCWL = (float) (7.4 / ((d + 10.) * (d - 3.)));
		float DDWL = (float) (4.8 / (d - 1));

		// Compute penalty[]    
		float dx = CCWL * (xs[d - 2 + xoffs] - xs[1 + xoffs]);
		float dy = CCWL * (ys[d - 2 + yoffs] - ys[1 + yoffs]);
		float pnlty = 0;
		for (int r = d / 2; r >= 0; r--, pnlty += dx) {
			penalty[r] = pnlty;
			penalty[d - 1 - r] = pnlty;
		}
		pnlty = dy;
		for (int r = d / 2 - 1; r >= 0; r--, pnlty += dy) {
			penalty[s[r]] += pnlty;
			penalty[s[d - 1 - r]] += pnlty;
		}
		//#define CCWL 0.16
		//    for (r=0; r<d; r++)
		//        penalty[r] = abs(d-1-r-r)*dx + abs(d-1-si[r]-si[r])*dy;

		// Compute distx[], disty[]
		int mins, maxs, minsi, maxsi;
		int[] distx = new int[MAXD];
		int[] disty = new int[MAXD];
		int xydiff = (xs[d - 1 + xoffs] - xs[xoffs]) - (ys[d - 1 + yoffs] - ys[yoffs]);
		if (s[0] < s[1]) {
			mins = s[0];
			maxs = s[1];
		} else {
			mins = s[1];
			maxs = s[0];
		}
		if (si[0] < si[1]) {
			minsi = si[0];
			maxsi = si[1];
		} else {
			minsi = si[1];
			maxsi = si[0];
		}
		for (int r = 2; r <= ub; r++) {
			if (s[r] < mins)
				mins = s[r];
			else if (s[r] > maxs)
				maxs = s[r];
			distx[r] = xs[maxs + xoffs] - xs[mins + xoffs];
			if (si[r] < minsi)
				minsi = si[r];
			else if (si[r] > maxsi)
				maxsi = si[r];
			disty[r] = ys[maxsi + yoffs] - ys[minsi + yoffs] + xydiff;
		}

		if (s[d - 2] < s[d - 1]) {
			mins = s[d - 2];
			maxs = s[d - 1];
		} else {
			mins = s[d - 1];
			maxs = s[d - 2];
		}
		if (si[d - 2] < si[d - 1]) {
			minsi = si[d - 2];
			maxsi = si[d - 1];
		} else {
			minsi = si[d - 1];
			maxsi = si[d - 2];
		}
		for (int r = d - 3; r >= lb; r--) {
			if (s[r] < mins)
				mins = s[r];
			else if (s[r] > maxs)
				maxs = s[r];
			distx[r] += xs[maxs + xoffs] - xs[mins + xoffs];
			if (si[r] < minsi)
				minsi = si[r];
			else if (si[r] > maxsi)
				maxsi = si[r];
			disty[r] += ys[maxsi + yoffs] - ys[minsi + yoffs];
		}

		int nbp = 0;
		for (int r = lb; r <= ub; r++) {
			if (si[r] == 0 || si[r] == d - 1) {
				score[nbp] = (xs[r + 1 + xoffs] - xs[r - 1 + xoffs])
						- penalty[r]
						- (int) AAWL
						* (ys[d - 2 + yoffs] - ys[1 + yoffs])
						- DDWL
						* disty[r];
			} else {
				score[nbp] = (xs[r + 1 + xoffs] - xs[r - 1 + xoffs])
						- penalty[r]
						- (int) BBWL
						* (ys[si[r] + 1 + yoffs] - ys[si[r] - 1 + yoffs])
						- DDWL
						* disty[r];
			}
			nbp++;

			if (s[r] == 0 || s[r] == d - 1) {
				score[nbp] = (ys[r + 1 + yoffs] - ys[r - 1 + yoffs])
						- penalty[s[r]]
						- (int) AAWL
						* (xs[d - 2 + xoffs] - xs[1 + xoffs])
						- DDWL
						* distx[r];
			} else {
				score[nbp] = (ys[r + 1 + yoffs] - ys[r - 1 + yoffs])
						- penalty[s[r]]
						- (int) BBWL
						* (xs[s[r] + 1 + xoffs] - xs[s[r] - 1 + xoffs])
						- DDWL
						* distx[r];
			}
			nbp++;
		}

		int newacc;
		if (acc <= 3)
			newacc = 1;
		else {
			newacc = acc / 2;
			if (acc >= nbp)
				acc = nbp - 1;
		}

		int minl = Integer.MAX_VALUE;
		int[] x2 = new int[MAXD];
		int[] y2 = new int[MAXD];
		int ll;
		int extral = 0;
		for (int i = 0; i < acc; i++) {
			int maxbp = 0;
			for (int bp = 1; bp < nbp; bp++)
				if (score[maxbp] < score[bp])
					maxbp = bp;
			score[maxbp] = (int) -9e9;

			int p = BreakPt(maxbp, lb);
			// Breaking in p
			if (BreakInX(maxbp)) { // break in x
				int n1 = 0, n2 = 0;
				for (int r = 0; r < d; r++) {
					if (s[r] < p) {
						s1[n1] = s[r];
						y1[n1] = ys[r + yoffs];
						n1++;
					} else if (s[r] > p) {
						s2[n2] = s[r] - p;
						y2[n2] = ys[r + yoffs];
						n2++;
					} else { // if (s[r] == p)  i.e.,  r = si[p]
						s1[n1] = p;
						s2[n2] = 0;
						if (r == d - 1 || r == d - 2) {
							y1[n1] = y2[n2] = ys[r - 1 + yoffs];
							extral = ys[r + yoffs] - ys[r - 1 + yoffs];
						}
						if (r == 0 || r == 1) {
							y1[n1] = y2[n2] = ys[r + 1 + yoffs];
							extral = ys[r + 1 + yoffs] - ys[r + yoffs];
						} else {
							y1[n1] = y2[n2] = ys[r + yoffs];
							extral = 0;
						}
						n1++;
						n2++;
					}
				}
				ll = extral
						+ flutes_wl_LMD(p + 1, xs, xoffs, y1, 0, s1, newacc)
						+ flutes_wl_LMD(d - p, xs, p + xoffs, y2, 0, s2, newacc);
			} else { // if (!BreakInX(maxbp))
				int n1 = 0, n2 = 0;
				for (int r = 0; r < d; r++) {
					if (si[r] < p) {
						s1[si[r]] = n1;
						x1[n1] = xs[r + xoffs];
						n1++;
					} else if (si[r] > p) {
						s2[si[r] - p] = n2;
						x2[n2] = xs[r + xoffs];
						n2++;
					} else { // if (si[r] == p)  i.e.,  r = s[p]
						s1[p] = n1;
						s2[0] = n2;
						if (r == d - 1 || r == d - 2) {
							x1[n1] = x2[n2] = xs[r - 1 + xoffs];
							extral = xs[r + xoffs] - xs[r - 1 + xoffs];
						}
						if (r == 0 || r == 1) {
							x1[n1] = x2[n2] = xs[r + 1 + xoffs];
							extral = xs[r + 1 + xoffs] - xs[r + xoffs];
						} else {
							x1[n1] = x2[n2] = xs[r + xoffs];
							extral = 0;
						}
						n1++;
						n2++;
					}
				}
				ll = extral
						+ flutes_wl_LMD(p + 1, x1, 0, ys, yoffs, s1, newacc)
						+ flutes_wl_LMD(d - p, x2, 0, ys, p + yoffs, s2, newacc);
			}
			if (minl > ll)
				minl = ll;
		}
		return minl;
	}

	public Tree flute(int d, int x[], int y[], int acc) {
		Tree t;

		if (d == 2) {
			t = new Tree();
			t.deg = 2;
			t.length = ADIFF(x[0], x[1]) + ADIFF(y[0], y[1]);
			t.branch = newBranches(2);
			t.branch[0].x = x[0];
			t.branch[0].y = y[0];
			t.branch[0].n = 1;
			t.branch[1].x = x[1];
			t.branch[1].y = y[1];
			t.branch[1].n = 1;
		} else {
			final Point[] pt = newPointsMAXD();
			final Point[] ptp = new Point[MAXD];
			for (int i = 0; i < d; i++) {
				pt[i].x = x[i];
				pt[i].y = y[i];
				ptp[i] = pt[i];
			}

			// sort x
			for (int i = 0; i < d - 1; i++) {
				int minval = ptp[i].x;
				int minidx = i;
				for (int j = i + 1; j < d; j++) {
					if (minval > ptp[j].x) {
						minval = ptp[j].x;
						minidx = j;
					}
				}
				Point tmpp = ptp[i];
				ptp[i] = ptp[minidx];
				ptp[minidx] = tmpp;
			}

			if (REMOVE_DUPLICATE_PIN) {
				ptp[d] = pt[d];
				ptp[d].x = ptp[d].y = -999999;
				int j = 0;
				for (int i = 0; i < d; i++) {
					int k;
					for (k = i + 1; ptp[k].x == ptp[i].x; k++)
						if (ptp[k].y == ptp[i].y) // pins k and i are the same
							break;
					if (ptp[k].x != ptp[i].x)
						ptp[j++] = ptp[i];
				}
				d = j;
			}

			int[] xs = new int[MAXD];
			int[] ys = new int[MAXD];
			for (int i = 0; i < d; i++) {
				xs[i] = ptp[i].x;
				ptp[i].o = i;
			}

			// sort y to find s[]
			int[] s = new int[MAXD];
			for (int i = 0; i < d - 1; i++) {
				int minval = ptp[i].y;
				int minidx = i;
				for (int j = i + 1; j < d; j++) {
					if (minval > ptp[j].y) {
						minval = ptp[j].y;
						minidx = j;
					}
				}
				ys[i] = ptp[minidx].y;
				s[i] = ptp[minidx].o;
				ptp[minidx] = ptp[i];
			}
			ys[d - 1] = ptp[d - 1].y;
			s[d - 1] = ptp[d - 1].o;

			t = flutes(d, xs, ys, s, acc);

			pointsPool.recycle(pt);
		}
		return t;
	}

	// xs[] and ys[] are coords in x and y in sorted order
	// s[] is a list of nodes in increasing y direction
	//   if nodes are indexed in the order of increasing x coord
	//   i.e., s[i] = s_i as defined in paper
	// The points are (xs[s[i]], ys[i]) for i=0..d-1
	//             or (xs[i], ys[si[i]]) for i=0..d-1

	public Tree flutes(int d, int xs[], int ys[], int s[], int acc) {
		if (REMOVE_DUPLICATE_PIN) {
			return flutes_RDP(d, xs, ys, s, acc);
		} else {
			return flutes_ALLD(d, xs, ys, s, acc);
		}
	}

	private Tree flutes_RDP(int d, int xs[], int ys[], int s[], int acc) {
		for (int i = 0; i < d - 1; i++) {
			if (xs[s[i]] == xs[s[i + 1]] && ys[i] == ys[i + 1]) {
				int ss;
				if (s[i] < s[i + 1])
					ss = s[i + 1];
				else {
					ss = s[i];
					s[i] = s[i + 1];
				}
				for (int j = i + 2; j < d; j++) {
					ys[j - 1] = ys[j];
					s[j - 1] = s[j];
				}
				for (int j = ss + 1; j < d; j++)
					xs[j - 1] = xs[j];
				for (int j = 0; j <= d - 2; j++)
					if (s[j] > ss)
						s[j]--;
				i--;
				d--;
			}
		}
		return flutes_ALLD(d, xs, ys, s, acc);
	}

	// For low-degree, i.e., 2 <= d <= D
	private Tree flutes_LD(int d, int xs[], int xoffs, int ys[], int yoffs, int s[]) {
		int minl;
		Tree t = new Tree();

		t.deg = d;
		t.branch = newBranches(2 * d - 2);
		if (d == 2) {
			minl = xs[1 + xoffs] - xs[xoffs] + ys[1 + yoffs] - ys[yoffs];
			t.branch[0].x = xs[s[0] + xoffs];
			t.branch[0].y = ys[yoffs];
			t.branch[0].n = 1;
			t.branch[1].x = xs[s[1] + xoffs];
			t.branch[1].y = ys[1 + yoffs];
			t.branch[1].n = 1;
		} else if (d == 3) {
			minl = xs[2 + xoffs] - xs[xoffs] + ys[2 + yoffs] - ys[yoffs];
			t.branch[0].x = xs[s[0] + xoffs];
			t.branch[0].y = ys[yoffs];
			t.branch[0].n = 3;
			t.branch[1].x = xs[s[1] + xoffs];
			t.branch[1].y = ys[1 + yoffs];
			t.branch[1].n = 3;
			t.branch[2].x = xs[s[2] + xoffs];
			t.branch[2].y = ys[2 + yoffs];
			t.branch[2].n = 3;
			t.branch[3].x = xs[1 + xoffs];
			t.branch[3].y = ys[1 + yoffs];
			t.branch[3].n = 3;
		} else {
			int k = 0;
			if (s[0] < s[2])
				k++;
			if (s[1] < s[2])
				k++;

			for (int i = 3; i <= d - 1; i++) { // p0=0 always, skip i=1 for symmetry
				int pi = s[i];
				for (int j = d - 1; j > i; j--)
					if (s[j] < s[i])
						pi--;
				k = pi + (i + 1) * k;
			}

			int[] dd = new int[2 * D - 2]; // 0..D-2 for v, D-1..2*D-3 for h
			boolean hflip;
			if (k < numgrp[d]) { // no horizontal flip
				hflip = false;
				for (int i = 1; i <= d - 3; i++) {
					dd[i] = ys[i + 1 + yoffs] - ys[i + yoffs];
					dd[d - 1 + i] = xs[i + 1 + xoffs] - xs[i + xoffs];
				}
			} else {
				hflip = true;
				k = 2 * numgrp[d] - 1 - k;
				for (int i = 1; i <= d - 3; i++) {
					dd[i] = ys[i + 1 + yoffs] - ys[i + yoffs];
					dd[d - 1 + i] = xs[d - 1 - i + xoffs] - xs[d - 2 - i + xoffs];
				}
			}

			int[] l = new int[MPOWV + 1];
			minl = l[0] = xs[d - 1 + xoffs] - xs[0 + xoffs] + ys[d - 1 + yoffs] - ys[yoffs];
			Csoln[] rlist = LUT[d][k];
			Csoln r = rlist[0];
			for (int i = 0; r.seg[i] > 0; i++)
				minl += dd[r.seg[i]];
			Csoln bestr = r;
			l[1] = minl;
			int j = 2;
			int rlistOffset = 0; // Java workaround for C-style pointer arithmetic on rlist
			while (j <= numsoln[d][k]) {
				rlistOffset++;
				r = rlist[rlistOffset];
				int sum = l[r.parent];
				for (int i = 0; r.seg[i] > 0; i++)
					sum += dd[r.seg[i]];
				for (int i = 10; r.seg[i] > 0; i--)
					sum -= dd[r.seg[i]];
				if (sum < minl) {
					minl = sum;
					bestr = r;
				}
				l[j++] = sum;
			}

			t.branch[0].x = xs[s[0] + xoffs];
			t.branch[0].y = ys[0 + yoffs];
			t.branch[1].x = xs[s[1] + xoffs];
			t.branch[1].y = ys[1 + yoffs];
			for (int i = 2; i < d - 2; i++) {
				t.branch[i].x = xs[s[i] + xoffs];
				t.branch[i].y = ys[i + yoffs];
				t.branch[i].n = bestr.neighbor[i];
			}
			t.branch[d - 2].x = xs[s[d - 2] + xoffs];
			t.branch[d - 2].y = ys[d - 2 + yoffs];
			t.branch[d - 1].x = xs[s[d - 1] + xoffs];
			t.branch[d - 1].y = ys[d - 1 + yoffs];
			if (hflip) {
				if (s[1] < s[0]) {
					t.branch[0].n = bestr.neighbor[1];
					t.branch[1].n = bestr.neighbor[0];
				} else {
					t.branch[0].n = bestr.neighbor[0];
					t.branch[1].n = bestr.neighbor[1];
				}
				if (s[d - 1] < s[d - 2]) {
					t.branch[d - 2].n = bestr.neighbor[d - 1];
					t.branch[d - 1].n = bestr.neighbor[d - 2];
				} else {
					t.branch[d - 2].n = bestr.neighbor[d - 2];
					t.branch[d - 1].n = bestr.neighbor[d - 1];
				}
				for (int i = d; i < 2 * d - 2; i++) {
					t.branch[i].x = xs[d - 1 - bestr.rowcol[i - d] % 16 + xoffs];
					t.branch[i].y = ys[bestr.rowcol[i - d] / 16 + yoffs];
					t.branch[i].n = bestr.neighbor[i];
				}
			} else { // !hflip
				if (s[0] < s[1]) {
					t.branch[0].n = bestr.neighbor[1];
					t.branch[1].n = bestr.neighbor[0];
				} else {
					t.branch[0].n = bestr.neighbor[0];
					t.branch[1].n = bestr.neighbor[1];
				}
				if (s[d - 2] < s[d - 1]) {
					t.branch[d - 2].n = bestr.neighbor[d - 1];
					t.branch[d - 1].n = bestr.neighbor[d - 2];
				} else {
					t.branch[d - 2].n = bestr.neighbor[d - 2];
					t.branch[d - 1].n = bestr.neighbor[d - 1];
				}
				for (int i = d; i < 2 * d - 2; i++) {
					t.branch[i].x = xs[bestr.rowcol[i - d] % 16 + xoffs];
					t.branch[i].y = ys[bestr.rowcol[i - d] / 16 + yoffs];
					t.branch[i].n = bestr.neighbor[i];
				}
			}
		}
		t.length = minl;

		return t;
	}

	// For medium-degree, i.e., D+1 <= d
	private Tree flutes_MD(int d, int xs[], int xoffs, int ys[], int yoffs, int s[], int acc) {
		int[] x1 = new int[MAXD];
		int[] y1 = new int[MAXD];
		int[] s1 = new int[MAXD];
		int[] s2 = new int[MAXD];

		if (s[0] < s[d - 1]) {
			int ms = max(s[0], s[1]);
			for (int i = 2; i <= ms; i++)
				ms = max(ms, s[i]);
			if (ms <= d - 3) {
				for (int i = 0; i <= ms; i++) {
					x1[i] = xs[i + xoffs];
					y1[i] = ys[i + yoffs];
					s1[i] = s[i];
				}
				x1[ms + 1] = xs[ms + xoffs];
				y1[ms + 1] = ys[ms + yoffs];
				s1[ms + 1] = ms + 1;

				s2[0] = 0;
				for (int i = 1; i <= d - 1 - ms; i++)
					s2[i] = s[i + ms] - ms;

				Tree t1 = flutes_LMD(ms + 2, x1, y1, s1, acc);
				Tree t2 = flutes_LMD(d - ms, xs, ms + xoffs, ys, ms + yoffs, s2, acc);
				Tree t = dmergetree(t1, t2);
				t1.branch = null;
				t2.branch = null;

				return t;
			}
		} else { // (s[0] > s[d-1])
			int ms = min(s[0], s[1]);
			for (int i = 2; i <= d - 1 - ms; i++)
				ms = min(ms, s[i]);
			if (ms >= 2) {
				x1[0] = xs[ms + xoffs];
				y1[0] = ys[0 + yoffs];
				s1[0] = s[0] - ms + 1;
				for (int i = 1; i <= d - 1 - ms; i++) {
					x1[i] = xs[i + ms - 1 + xoffs];
					y1[i] = ys[i + yoffs];
					s1[i] = s[i] - ms + 1;
				}
				x1[d - ms] = xs[d - 1 + xoffs];
				y1[d - ms] = ys[d - 1 - ms + yoffs];
				s1[d - ms] = 0;

				s2[0] = ms;
				for (int i = 1; i <= ms; i++)
					s2[i] = s[i + d - 1 - ms];

				Tree t1 = flutes_LMD(d + 1 - ms, x1, y1, s1, acc);
				Tree t2 = flutes_LMD(ms + 1, xs, 0 + xoffs, ys, d - 1 - ms + yoffs, s2, acc);
				Tree t = dmergetree(t1, t2);
				t1.branch = null;
				t2.branch = null;

				return t;
			}
		}

		// Find inverse si[] of s[]
		int[] si = new int[MAXD];
		for (int r = 0; r < d; r++)
			si[s[r]] = r;

		// Determine breaking directions and positions dp[]
		int lb = (d - 2 * acc + 2) / 4;
		if (lb < 2)
			lb = 2;
		int ub = d - 1 - lb;

		// Compute scores    
		final float AA = 0.6f; // 2.0*BB
		final float BB = 0.3f;
		float CC = (float) (7.4 / ((d + 10.) * (d - 3.)));
		float DD = (float) (4.8 / (d - 1));

		// Compute penalty[]    
		float[] penalty = new float[MAXD];
		float pnlty = 0;
		float dx = CC * (xs[d - 2 + xoffs] - xs[1 + xoffs]);
		float dy = CC * (ys[d - 2 + yoffs] - ys[1 + yoffs]);
		for (int r = d / 2; r >= 2; r--, pnlty += dx) {
			penalty[r] = pnlty;
			penalty[d - 1 - r] = pnlty;
		}
		penalty[1] = pnlty;
		penalty[d - 2] = pnlty;
		penalty[0] = pnlty;
		penalty[d - 1] = pnlty;
		pnlty = dy;
		for (int r = d / 2 - 1; r >= 2; r--, pnlty += dy) {
			penalty[s[r]] += pnlty;
			penalty[s[d - 1 - r]] += pnlty;
		}
		penalty[s[1]] += pnlty;
		penalty[s[d - 2]] += pnlty;
		penalty[s[0]] += pnlty;
		penalty[s[d - 1]] += pnlty;
		//#define CC 0.16
		//#define v(r) ((r==0||r==1||r==d-2||r==d-1) ? d-3 : abs(d-1-r-r))
		//    for (r=0; r<d; r++)
		//        penalty[r] = v(r)*dx + v(si[r])*dy;

		// Compute distx[], disty[]
		int[] distx = new int[MAXD];
		int[] disty = new int[MAXD];
		int xydiff = (xs[d - 1 + xoffs] - xs[0 + xoffs]) - (ys[d - 1 + yoffs] - ys[0 + yoffs]);
		int mins, maxs, minsi, maxsi;
		if (s[0] < s[1]) {
			mins = s[0];
			maxs = s[1];
		} else {
			mins = s[1];
			maxs = s[0];
		}
		if (si[0] < si[1]) {
			minsi = si[0];
			maxsi = si[1];
		} else {
			minsi = si[1];
			maxsi = si[0];
		}
		for (int r = 2; r <= ub; r++) {
			if (s[r] < mins)
				mins = s[r];
			else if (s[r] > maxs)
				maxs = s[r];
			distx[r] = xs[maxs + xoffs] - xs[mins + xoffs];
			if (si[r] < minsi)
				minsi = si[r];
			else if (si[r] > maxsi)
				maxsi = si[r];
			disty[r] = ys[maxsi + yoffs] - ys[minsi + yoffs] + xydiff;
		}

		if (s[d - 2] < s[d - 1]) {
			mins = s[d - 2];
			maxs = s[d - 1];
		} else {
			mins = s[d - 1];
			maxs = s[d - 2];
		}
		if (si[d - 2] < si[d - 1]) {
			minsi = si[d - 2];
			maxsi = si[d - 1];
		} else {
			minsi = si[d - 1];
			maxsi = si[d - 2];
		}
		for (int r = d - 3; r >= lb; r--) {
			if (s[r] < mins)
				mins = s[r];
			else if (s[r] > maxs)
				maxs = s[r];
			distx[r] += xs[maxs + xoffs] - xs[mins + xoffs];
			if (si[r] < minsi)
				minsi = si[r];
			else if (si[r] > maxsi)
				maxsi = si[r];
			disty[r] += ys[maxsi + yoffs] - ys[minsi + yoffs];
		}

		int nbp = 0;
		float[] score = new float[2 * MAXD];
		for (int r = lb; r <= ub; r++) {
			if (si[r] <= 1)
				score[nbp] = (xs[r + 1 + xoffs] - xs[r - 1 + xoffs])
						- penalty[r]
						- (int) AA
						* (ys[2 + yoffs] - ys[1 + yoffs])
						- DD
						* disty[r];
			else if (si[r] >= d - 2)
				score[nbp] = (xs[r + 1 + xoffs] - xs[r - 1 + xoffs])
						- penalty[r]
						- (int) AA
						* (ys[d - 2 + yoffs] - ys[d - 3 + yoffs])
						- DD
						* disty[r];
			else
				score[nbp] = (xs[r + 1 + xoffs] - xs[r - 1 + xoffs])
						- penalty[r]
						- (int) BB
						* (ys[si[r] + 1 + yoffs] - ys[si[r] - 1 + yoffs])
						- DD
						* disty[r];
			nbp++;

			if (s[r] <= 1)
				score[nbp] = (ys[r + 1 + yoffs] - ys[r - 1 + yoffs])
						- penalty[s[r]]
						- (int) AA
						* (xs[2 + xoffs] - xs[1 + xoffs])
						- DD
						* distx[r];
			else if (s[r] >= d - 2)
				score[nbp] = (ys[r + 1 + yoffs] - ys[r - 1 + yoffs])
						- penalty[s[r]]
						- (int) AA
						* (xs[d - 2 + xoffs] - xs[d - 3 + xoffs])
						- DD
						* distx[r];
			else
				score[nbp] = (ys[r + 1 + yoffs] - ys[r - 1 + yoffs])
						- penalty[s[r]]
						- (int) BB
						* (xs[s[r] + 1 + xoffs] - xs[s[r] - 1 + xoffs])
						- DD
						* distx[r];
			nbp++;
		}

		int newacc;
		if (acc <= 3)
			newacc = 1;
		else {
			newacc = acc / 2;
			if (acc >= nbp)
				acc = nbp - 1;
		}

		int minl = Integer.MAX_VALUE;
		Tree bestt1 = new Tree();
		Tree bestt2 = new Tree();
		bestt1.branch = bestt2.branch = null;
		int[] x2 = new int[MAXD];
		int[] y2 = new int[MAXD];
		int bestbp = 0;
		for (int i = 0; i < acc; i++) {
			int maxbp = 0;
			for (int bp = 1; bp < nbp; bp++)
				if (score[maxbp] < score[bp])
					maxbp = bp;
			score[maxbp] = (int) -9e9;

			int p = BreakPt(maxbp, lb);
			// Breaking in p
			int ll;
			Tree t1, t2;
			if (BreakInX(maxbp)) { // break in x
				int n1 = 0, n2 = 0, nn1 = 0, nn2 = 0;
				for (int r = 0; r < d; r++) {
					if (s[r] < p) {
						s1[n1] = s[r];
						y1[n1] = ys[r + yoffs];
						n1++;
					} else if (s[r] > p) {
						s2[n2] = s[r] - p;
						y2[n2] = ys[r + yoffs];
						n2++;
					} else { // if (s[r] == p)  i.e.,  r = si[p]
						s1[n1] = p;
						s2[n2] = 0;
						y1[n1] = y2[n2] = ys[r + yoffs];
						nn1 = n1;
						nn2 = n2;
						n1++;
						n2++;
					}
				}

				t1 = flutes_LMD(p + 1, xs, xoffs, y1, 0, s1, newacc);
				t2 = flutes_LMD(d - p, xs, p + xoffs, y2, 0, s2, newacc);
				ll = (t1.length + t2.length);
				int coord1 = t1.branch[t1.branch[nn1].n].y;
				int coord2 = t2.branch[t2.branch[nn2].n].y;
				if (t2.branch[nn2].y > max(coord1, coord2))
					ll -= t2.branch[nn2].y - max(coord1, coord2);
				else if (t2.branch[nn2].y < min(coord1, coord2))
					ll -= min(coord1, coord2) - t2.branch[nn2].y;
			} else { // if (!BreakInX(maxbp))
				int n1 = 0, n2 = 0;
				for (int r = 0; r < d; r++) {
					if (si[r] < p) {
						s1[si[r]] = n1;
						x1[n1] = xs[r + xoffs];
						n1++;
					} else if (si[r] > p) {
						s2[si[r] - p] = n2;
						x2[n2] = xs[r + xoffs];
						n2++;
					} else { // if (si[r] == p)  i.e.,  r = s[p]
						s1[p] = n1;
						s2[0] = n2;
						x1[n1] = x2[n2] = xs[r + xoffs];
						n1++;
						n2++;
					}
				}

				t1 = flutes_LMD(p + 1, x1, 0, ys, yoffs, s1, newacc);
				t2 = flutes_LMD(d - p, x2, 0, ys, p + yoffs, s2, newacc);
				ll = (t1.length + t2.length);
				int coord1 = t1.branch[t1.branch[p].n].x;
				int coord2 = t2.branch[t2.branch[0].n].x;
				if (t2.branch[0].x > max(coord1, coord2))
					ll -= t2.branch[0].x - max(coord1, coord2);
				else if (t2.branch[0].x < min(coord1, coord2))
					ll -= min(coord1, coord2) - t2.branch[0].x;
			}
			if (minl > ll) {
				minl = ll;
				bestt1.branch = null;
				bestt2.branch = null;
				bestt1 = t1;
				bestt2 = t2;
				bestbp = maxbp;
			} else {
				t1.branch = null;
				t2.branch = null;
			}
		}

		Tree t;
		if (LOCAL_REFINEMENT) {
			if (BreakInX(bestbp)) {
				t = hmergetree(bestt1, bestt2, s);
				local_refinement(t, si[BreakPt(bestbp, lb)]);
			} else {
				t = vmergetree(bestt1, bestt2);
				local_refinement(t, BreakPt(bestbp, lb));
			}
		} else {
			if (BreakInX(bestbp)) {
				t = hmergetree(bestt1, bestt2, s);
			} else {
				t = vmergetree(bestt1, bestt2);
			}
		}

		bestt1.branch = null;
		bestt2.branch = null;

		return t;
	}

	private Tree dmergetree(Tree t1, Tree t2) {
		int d;

		Tree t = new Tree();
		t.deg = d = t1.deg + t2.deg - 2;
		t.length = t1.length + t2.length;
		t.branch = newBranches(2 * d - 2);

		int offset1 = t2.deg - 2;
		int offset2 = 2 * t1.deg - 4;

		for (int i = 0; i <= t1.deg - 2; i++) {
			t.branch[i].x = t1.branch[i].x;
			t.branch[i].y = t1.branch[i].y;
			t.branch[i].n = t1.branch[i].n + offset1;
		}
		for (int i = t1.deg - 1; i <= d - 1; i++) {
			t.branch[i].x = t2.branch[i - t1.deg + 2].x;
			t.branch[i].y = t2.branch[i - t1.deg + 2].y;
			t.branch[i].n = t2.branch[i - t1.deg + 2].n + offset2;
		}
		for (int i = d; i <= d + t1.deg - 3; i++) {
			t.branch[i].x = t1.branch[i - offset1].x;
			t.branch[i].y = t1.branch[i - offset1].y;
			t.branch[i].n = t1.branch[i - offset1].n + offset1;
		}
		for (int i = d + t1.deg - 2; i <= 2 * d - 3; i++) {
			t.branch[i].x = t2.branch[i - offset2].x;
			t.branch[i].y = t2.branch[i - offset2].y;
			t.branch[i].n = t2.branch[i - offset2].n + offset2;
		}

		int prev = t2.branch[0].n + offset2;
		int curr = t1.branch[t1.deg - 1].n + offset1;
		int next = t.branch[curr].n;
		while (curr != next) {
			t.branch[curr].n = prev;
			prev = curr;
			curr = next;
			next = t.branch[curr].n;
		}
		t.branch[curr].n = prev;

		return t;
	}

	private Tree hmergetree(Tree t1, Tree t2, int s[]) {
		int ii = 0, nn1 = 0, nn2 = 0;
		Tree t = new Tree();

		t.deg = t1.deg + t2.deg - 1;
		t.length = t1.length + t2.length;
		t.branch = newBranches(2 * t.deg - 2);
		int offset1 = t2.deg - 1;
		int offset2 = 2 * t1.deg - 3;

		int p = t1.deg - 1;
		int n1 = 0, n2 = 0;
		for (int i = 0; i < t.deg; i++) {
			if (s[i] < p) {
				t.branch[i].x = t1.branch[n1].x;
				t.branch[i].y = t1.branch[n1].y;
				t.branch[i].n = t1.branch[n1].n + offset1;
				n1++;
			} else if (s[i] > p) {
				t.branch[i].x = t2.branch[n2].x;
				t.branch[i].y = t2.branch[n2].y;
				t.branch[i].n = t2.branch[n2].n + offset2;
				n2++;
			} else {
				t.branch[i].x = t2.branch[n2].x;
				t.branch[i].y = t2.branch[n2].y;
				t.branch[i].n = t2.branch[n2].n + offset2;
				nn1 = n1;
				nn2 = n2;
				ii = i;
				n1++;
				n2++;
			}
		}
		for (int i = t.deg; i <= t.deg + t1.deg - 3; i++) {
			t.branch[i].x = t1.branch[i - offset1].x;
			t.branch[i].y = t1.branch[i - offset1].y;
			t.branch[i].n = t1.branch[i - offset1].n + offset1;
		}
		for (int i = t.deg + t1.deg - 2; i <= 2 * t.deg - 4; i++) {
			t.branch[i].x = t2.branch[i - offset2].x;
			t.branch[i].y = t2.branch[i - offset2].y;
			t.branch[i].n = t2.branch[i - offset2].n + offset2;
		}
		int extra = 2 * t.deg - 3;
		int coord1 = t1.branch[t1.branch[nn1].n].y;
		int coord2 = t2.branch[t2.branch[nn2].n].y;
		if (t2.branch[nn2].y > max(coord1, coord2)) {
			t.branch[extra].y = max(coord1, coord2);
			t.length -= t2.branch[nn2].y - t.branch[extra].y;
		} else if (t2.branch[nn2].y < min(coord1, coord2)) {
			t.branch[extra].y = min(coord1, coord2);
			t.length -= t.branch[extra].y - t2.branch[nn2].y;
		} else
			t.branch[extra].y = t2.branch[nn2].y;
		t.branch[extra].x = t2.branch[nn2].x;
		t.branch[extra].n = t.branch[ii].n;
		t.branch[ii].n = extra;

		int prev = extra;
		int curr = t1.branch[nn1].n + offset1;
		int next = t.branch[curr].n;
		while (curr != next) {
			t.branch[curr].n = prev;
			prev = curr;
			curr = next;
			next = t.branch[curr].n;
		}
		t.branch[curr].n = prev;

		return t;
	}

	private Tree vmergetree(Tree t1, Tree t2) {
		Tree t = new Tree();

		t.deg = t1.deg + t2.deg - 1;
		t.length = t1.length + t2.length;
		t.branch = newBranches(2 * t.deg - 2);
		int offset1 = t2.deg - 1;
		int offset2 = 2 * t1.deg - 3;

		for (int i = 0; i <= t1.deg - 2; i++) {
			t.branch[i].x = t1.branch[i].x;
			t.branch[i].y = t1.branch[i].y;
			t.branch[i].n = t1.branch[i].n + offset1;
		}
		for (int i = t1.deg - 1; i <= t.deg - 1; i++) {
			t.branch[i].x = t2.branch[i - t1.deg + 1].x;
			t.branch[i].y = t2.branch[i - t1.deg + 1].y;
			t.branch[i].n = t2.branch[i - t1.deg + 1].n + offset2;
		}
		for (int i = t.deg; i <= t.deg + t1.deg - 3; i++) {
			t.branch[i].x = t1.branch[i - offset1].x;
			t.branch[i].y = t1.branch[i - offset1].y;
			t.branch[i].n = t1.branch[i - offset1].n + offset1;
		}
		for (int i = t.deg + t1.deg - 2; i <= 2 * t.deg - 4; i++) {
			t.branch[i].x = t2.branch[i - offset2].x;
			t.branch[i].y = t2.branch[i - offset2].y;
			t.branch[i].n = t2.branch[i - offset2].n + offset2;
		}
		int extra = 2 * t.deg - 3;
		int coord1 = t1.branch[t1.branch[t1.deg - 1].n].x;
		int coord2 = t2.branch[t2.branch[0].n].x;
		if (t2.branch[0].x > max(coord1, coord2)) {
			t.branch[extra].x = max(coord1, coord2);
			t.length -= t2.branch[0].x - t.branch[extra].x;
		} else if (t2.branch[0].x < min(coord1, coord2)) {
			t.branch[extra].x = min(coord1, coord2);
			t.length -= t.branch[extra].x - t2.branch[0].x;
		} else
			t.branch[extra].x = t2.branch[0].x;
		t.branch[extra].y = t2.branch[0].y;
		t.branch[extra].n = t.branch[t1.deg - 1].n;
		t.branch[t1.deg - 1].n = extra;

		int prev = extra;
		int curr = t1.branch[t1.deg - 1].n + offset1;
		int next = t.branch[curr].n;
		while (curr != next) {
			t.branch[curr].n = prev;
			prev = curr;
			curr = next;
			next = t.branch[curr].n;
		}
		t.branch[curr].n = prev;

		return t;
	}

	private void local_refinement(Tree tp, int p) {
		int d = tp.deg;
		int root = tp.branch[p].n;

		// Reverse edges to point to root    
		int prev = root;
		int curr = tp.branch[prev].n;
		int next = tp.branch[curr].n;
		while (curr != next) {
			tp.branch[curr].n = prev;
			prev = curr;
			curr = next;
			next = tp.branch[curr].n;
		}
		tp.branch[curr].n = prev;
		tp.branch[root].n = root;

		// Find Steiner nodes that are at pins
		int[] SteinerPin = new int[2 * MAXD];
		for (int i = d; i <= 2 * d - 3; i++)
			SteinerPin[i] = -1;
		for (int i = 0; i < d; i++) {
			next = tp.branch[i].n;
			if (tp.branch[i].x == tp.branch[next].x && tp.branch[i].y == tp.branch[next].y)
				SteinerPin[next] = i; // Steiner 'next' at Pin 'i'
		}
		SteinerPin[root] = p;

		// Find pins that are directly connected to root    
		int dd = 0;
		int[] index = new int[2 * MAXD];
		int[] x = new int[MAXD];
		for (int i = 0; i < d; i++) {
			curr = tp.branch[i].n;
			if (SteinerPin[curr] == i)
				curr = tp.branch[curr].n;
			while (SteinerPin[curr] < 0)
				curr = tp.branch[curr].n;
			if (curr == root) {
				x[dd] = tp.branch[i].x;
				if (SteinerPin[tp.branch[i].n] == i && tp.branch[i].n != root)
					index[dd++] = tp.branch[i].n; // Steiner node
				else
					index[dd++] = i; // Pin
			}
		}

		if (4 <= dd && dd <= D) {
			// Find Steiner nodes that are directly connected to root    
			int ii = dd;
			for (int i = 0; i < dd; i++) {
				curr = tp.branch[index[i]].n;
				while (SteinerPin[curr] < 0) {
					index[ii++] = curr;
					SteinerPin[curr] = Integer.MAX_VALUE;
					curr = tp.branch[curr].n;
				}
			}
			index[ii] = root;

			int[] xs = new int[D];
			int[] ys = new int[D];
			int[] ss = new int[D];
			for (ii = 0; ii < dd; ii++) {
				ss[ii] = 0;
				for (int j = 0; j < ii; j++)
					if (x[j] < x[ii])
						ss[ii]++;
				for (int j = ii + 1; j < dd; j++)
					if (x[j] <= x[ii])
						ss[ii]++;
				xs[ss[ii]] = x[ii];
				ys[ii] = tp.branch[index[ii]].y;
			}

			Tree tt = flutes_LD(dd, xs, 0, ys, 0, ss);

			// Find new wirelength
			tp.length += tt.length;
			for (ii = 0; ii < 2 * dd - 3; ii++) {
				int i = index[ii];
				int j = tp.branch[i].n;
				tp.length -= ADIFF(tp.branch[i].x, tp.branch[j].x) + ADIFF(tp.branch[i].y, tp.branch[j].y);
			}

			// Copy tt into t
			for (ii = 0; ii < dd; ii++) {
				tp.branch[index[ii]].n = index[tt.branch[ii].n];
			}
			for (; ii <= 2 * dd - 3; ii++) {
				tp.branch[index[ii]].x = tt.branch[ii].x;
				tp.branch[index[ii]].y = tt.branch[ii].y;
				tp.branch[index[ii]].n = index[tt.branch[ii].n];
			}
			tt.branch = null;
		}

	}

	public int wirelength(Tree t) {
		int l = 0;

		for (int i = 0; i < 2 * t.deg - 2; i++) {
			int j = t.branch[i].n;
			l += ADIFF(t.branch[i].x, t.branch[j].x) + ADIFF(t.branch[i].y, t.branch[j].y);
		}

		return l;
	}

	public void printtree(Tree t) {
		for (int i = 0; i < t.deg; i++) {
			System.out.printf(
					" %-2d:  x=%4g  y=%4g  e=%d",
					i,
					(float) t.branch[i].x,
					(float) t.branch[i].y,
					t.branch[i].n);
			System.out.println();
		}
		for (int i = t.deg; i < 2 * t.deg - 2; i++) {
			System.out.printf(
					"s%-2d:  x=%4g  y=%4g  e=%d",
					i,
					(float) t.branch[i].x,
					(float) t.branch[i].y,
					t.branch[i].n);
			System.out.println();
		}
		System.out.println();
	}

	// Output in a format that can be plotted by gnuplot
	public void plottree(Tree t) {
		for (int i = 0; i < 2 * t.deg - 2; i++) {
			System.out.printf("%d %d", t.branch[i].x, t.branch[i].y);
			System.out.println();
			System.out.printf("%d %d", t.branch[t.branch[i].n].x, t.branch[t.branch[i].n].y);
			System.out.println();
			System.out.println();
		}
	}

	private int BreakPt(int bp, int lb) {
		return bp / 2 + lb;
	}

	private boolean BreakInX(int bp) {
		return bp % 2 == 0;
	}

	private int flutes_wl_ALLD(int d, int xs[], int ys[], int s[], int acc) {
		return flutes_wl_LMD(d, xs, ys, s, acc);
	}

	private Tree flutes_ALLD(int d, int xs[], int ys[], int s[], int acc) {
		return flutes_LMD(d, xs, ys, s, acc);
	}

	private int flutes_wl_LMD(int d, int xs[], int ys[], int s[], int acc) {
		return d <= D ? flutes_wl_LD(d, xs, 0, ys, 0, s) : flutes_wl_MD(d, xs, 0, ys, 0, s, acc);
	}

	private int flutes_wl_LMD(int d, int xs[], int xoffs, int ys[], int yoffs, int s[], int acc) {
		return d <= D
				? flutes_wl_LD(d, xs, xoffs, ys, yoffs, s)
				: flutes_wl_MD(d, xs, xoffs, ys, yoffs, s, acc);
	}

	private Tree flutes_LMD(int d, int xs[], int ys[], int s[], int acc) {
		return d <= D ? flutes_LD(d, xs, 0, ys, 0, s) : flutes_MD(d, xs, 0, ys, 0, s, acc);
	}

	private Tree flutes_LMD(int d, int xs[], int xoffs, int ys[], int yoffs, int s[], int acc) {
		return d <= D ? flutes_LD(d, xs, xoffs, ys, yoffs, s) : flutes_MD(d, xs, xoffs, ys, yoffs, s, acc);
	}

	private Point[] newPointsMAXD() {
		Point[] points = pointsPool.get();
		if (points != null) {
			return points;
		} else {
			Point[] pts = new Point[MAXD];
			for (int i = 0; i < MAXD; i++) {
				pts[i] = new Point();
			}
			return pts;
		}
	}

	private Branch[] newBranches(int size) {
		Branch[] array = new Branch[size];
		for (int i = 0; i < size; i++) {
			array[i] = new Branch();
		}
		return array;
	}

	private static int max(int x, int y) {
		return x > y ? x : y;
	}

	private static int min(int x, int y) {
		return x < y ? x : y;
	}

	private static int ADIFF(int x, int y) {
		return x > y ? x - y : y - x; // Absolute difference
	}

	public static void main(String[] args) throws IOException {
		// Some code demonstrating the use of the API

		File lutDir = new File("<insert path to directory containing the LUT files>");

		int[] x = {3, 1, 2, 4, 6, 10, 7};
		int[] y = {1, 2, 4, 4, 7, 9, 11};

		int degree = x.length;

		Flute flute = new Flute();

		System.out.println("Reading LUT...");
		long start = System.currentTimeMillis();
		flute.readLUT(lutDir);
		System.out.println("Elapsed time: " + (System.currentTimeMillis() - start) + " ms");

		Tree flutetree = flute.flute(degree, x, y, ACCURACY);
		System.out.printf("FLUTE wirelength = %d\n", flutetree.length);

		int flutewl = flute.flute_wl(degree, x, y, ACCURACY);
		System.out.printf("FLUTE wirelength (without RSMT construction) = %d\n", flutewl);
	}

}