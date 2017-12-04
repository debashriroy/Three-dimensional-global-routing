package vlsi.routing.design.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import TwoVarRuleFis.TwoVarRuleFisClass;

public class MixedRouterProcessImplementation {
	String inpGRFileName;
	Integer noOfGridInXDir;
	Integer noOfGridInYDir;
	Integer noOfLayers;
	Integer lowerLeftXCoordinate;
	Integer lowerLeftYCoordinate;
	Integer gridWidth;
	Integer gridHeight;
	Integer numberOfCapacityAdjustment;
	Long totalNumberOfNet;
	float incrementOfIneligibilityPerNetRouting;
	int notRoutedCount;
	int totalWireLength = 0;
	
	List<Integer> verticalCapacity;
	List<Integer> horizontalCapacity;
	List<Integer> minimumWidth;
	List<Integer> minimumSpacing;
	List<Integer> viaSpacingList;
	List<List<Integer>> capacityAdjustmentList;
	List<List<Integer>> totalNetInformation;
	
	List<List<List<Integer>>> totalNetList;
		private List<List<List<Integer>>> multiplePinNetlistInformationList;
	private List<List<List<Integer>>> twoPinNetlistInformationList;
	
	TwoVarRuleFisClass ruleBaseClass = null;
	private float alphaParamTwoVar = (float) 0.6;
	private float betaParamTwoVar= (float) 0.4;
	private List<List<Float>> subregionInfoList;
	Map<String, Float> subregionMap;
	
	
	public Integer getNoOfGridInXDir() {
		return noOfGridInXDir;
	}
	public void setNoOfGridInXDir(Integer noOfGridInXDir) {
		this.noOfGridInXDir = noOfGridInXDir;
	}
	public Integer getNoOfGridInYDir() {
		return noOfGridInYDir;
	}
	public void setNoOfGridInYDir(Integer noOfGridInYDir) {
		this.noOfGridInYDir = noOfGridInYDir;
	}
	public Integer getNoOfLayers() {
		return noOfLayers;
	}
	public void setNoOfLayers(Integer noOfLayers) {
		this.noOfLayers = noOfLayers;
	}
	public Integer getLowerLeftXCoordinate() {
		return lowerLeftXCoordinate;
	}
	public void setLowerLeftXCoordinate(Integer lowerLeftXCoordinate) {
		this.lowerLeftXCoordinate = lowerLeftXCoordinate;
	}
	public Integer getLowerLeftYCoordinate() {
		return lowerLeftYCoordinate;
	}
	public void setLowerLeftYCoordinate(Integer lowerLeftYCoordinate) {
		this.lowerLeftYCoordinate = lowerLeftYCoordinate;
	}
	public Integer getGridWidth() {
		return gridWidth;
	}
	public void setGridWidth(Integer gridWidth) {
		this.gridWidth = gridWidth;
	}
	public Integer getGridHeight() {
		return gridHeight;
	}
	public void setGridHeight(Integer gridHeight) {
		this.gridHeight = gridHeight;
	}
	public Integer getNumberOfCapacityAdjustment() {
		return numberOfCapacityAdjustment;
	}
	public void setNumberOfCapacityAdjustment(Integer numberOfCapacityAdjustment) {
		this.numberOfCapacityAdjustment = numberOfCapacityAdjustment;
	}
	public Long getTotalNumberOfNet() {
		return totalNumberOfNet;
	}
	public void setTotalNumberOfNet(Long totalNumberOfNet) {
		this.totalNumberOfNet = totalNumberOfNet;
	}
	public List<Integer> getVerticalCapacity() {
		return verticalCapacity;
	}
	public void setVerticalCapacity(List<Integer> verticalCapacity) {
		this.verticalCapacity = verticalCapacity;
	}
	public List<Integer> getHorizontalCapacity() {
		return horizontalCapacity;
	}
	public void setHorizontalCapacity(List<Integer> horizontalCapacity) {
		this.horizontalCapacity = horizontalCapacity;
	}
	public List<Integer> getMinimumWidth() {
		return minimumWidth;
	}
	public void setMinimumWidth(List<Integer> minimumWidth) {
		this.minimumWidth = minimumWidth;
	}
	public List<Integer> getMinimumSpacing() {
		return minimumSpacing;
	}
	public void setMinimumSpacing(List<Integer> minimumSpacing) {
		this.minimumSpacing = minimumSpacing;
	}
	public List<List<Integer>> getCapacityAdjustmentList() {
		return capacityAdjustmentList;
	}
	public void setCapacityAdjustmentList(List<List<Integer>> capacityAdjustmentList) {
		this.capacityAdjustmentList = capacityAdjustmentList;
	}
	public List<List<Integer>> getTotalNetInformation() {
		return totalNetInformation;
	}
	public void setTotalNetInformation(List<List<Integer>> totalNetInformation) {
		this.totalNetInformation = totalNetInformation;
	}
	public List<List<List<Integer>>> getTotalNetList() {
		return totalNetList;
	}
	public void setTotalNetList(List<List<List<Integer>>> totalNetList) {
		this.totalNetList = totalNetList;
	}
	
	public String getInpGRFileName() {
		return inpGRFileName;
	}
	public void setInpGRFileName(String inpGRFileName) {
		this.inpGRFileName = inpGRFileName;
	}
	public List<Integer> getViaSpacingList() {
		return viaSpacingList;
	}
	public void setViaSpacingList(List<Integer> viaSpacingList) {
		this.viaSpacingList = viaSpacingList;
	}
	public float getAlphaParamTwoVar() {
		return alphaParamTwoVar;
	}
	public void setAlphaParamTwoVar(float alphaParamTwoVar) {
		this.alphaParamTwoVar = alphaParamTwoVar;
	}
	public float getBetaParamTwoVar() {
		return betaParamTwoVar;
	}
	public void setBetaParamTwoVar(float betaParamTwoVar) {
		this.betaParamTwoVar = betaParamTwoVar;
	}
	public List<List<Float>> getSubregionInfoList() {
		return subregionInfoList;
	}
	public void setSubregionInfoList(List<List<Float>> subregionInfoList) {
		this.subregionInfoList = subregionInfoList;
	}
	public Map<String, Float> getSubregionMap() {
		return subregionMap;
	}
	public void setSubregionMap(Map<String, Float> subregionMap) {
		this.subregionMap = subregionMap;
	}
	public List<List<List<Integer>>> getMultiplePinNetlistInformationList() {
		return multiplePinNetlistInformationList;
	}
	public void setMultiplePinNetlistInformationList(
			List<List<List<Integer>>> multiplePinNetlistInformationList) {
		this.multiplePinNetlistInformationList = multiplePinNetlistInformationList;
	}
	public List<List<List<Integer>>> getTwoPinIntraLayerNetlistInformationList() {
		return twoPinNetlistInformationList;
	}
	public void setTwoPinIntraLayerNetlistInformationList(
			List<List<List<Integer>>> twoPinIntraLayerNetlistInformationList) {
		this.twoPinNetlistInformationList = twoPinIntraLayerNetlistInformationList;
	}
	public void readGRFileAndPopulateParams(){
		
		totalNetInformation = new ArrayList<List<Integer>>();
		capacityAdjustmentList = new ArrayList<List<Integer>>();
		totalNetList = new ArrayList<List<List<Integer>>>();
		
		try{
		FileInputStream fstream = new FileInputStream(getInpGRFileName());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int lineCount=1;
		int prevLineCount=lineCount;
		List<List<Integer>> oneNetList= new ArrayList<List<Integer>>();
		while ((strLine = br.readLine()) != null) {
		//	System.out.println(strLine);
			
			
			if (strLine.contains("grid")) {//SETTING NUMBER OF GRIDS IN X DIRECTION AND Y DIRECTION AND THE NUMBER OF LAYERS
		//		System.out.println("grid:"+strLine);
				String str1 =strLine.substring(4).trim();
				setNoOfGridInXDir(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
				
				int ind2= str1.indexOf(" ");
				String str2 =str1.substring(ind2).trim();
				setNoOfGridInYDir(Integer.parseInt(str2.substring(0,str2.indexOf(" ")).trim()));
				
				int ind3= str2.indexOf(" ");
				String str3 =str2.substring(ind3).trim();
				setNoOfLayers(Integer.parseInt(str3.substring(0).trim()));
			}
			else if (strLine.contains("vertical capacity")) {////SETTING THE VERTICAL CAPACITY PER LAYER
				int ind=17;
				String str1 =strLine.substring(ind).trim();
				List<Integer> vertList = new ArrayList<Integer>();
				for(int i=0;i<getNoOfLayers()-1;i++){
					vertList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
					//ind= str1.indexOf(" ");
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				vertList.add(Integer.parseInt(str1));
				setVerticalCapacity(vertList);
			}
			else if (strLine.contains("horizontal capacity")) {////SETTING THE HORIZONTAL CAPACITY PER LAYER
				int ind=19;
				String str1 =strLine.substring(ind).trim();
				List<Integer> horList = new ArrayList<Integer>();
				for(int i=0;i<getNoOfLayers()-1;i++){
					horList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
				//	ind= str1.indexOf(" ");
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				horList.add(Integer.parseInt(str1));
				setHorizontalCapacity(horList);
			}
			else if (strLine.contains("minimum width")) {////SETTING THE MINIMUM WIDTH PER LAYER
				int ind=13;
				String str1 =strLine.substring(ind).trim();
				List<Integer> minWList = new ArrayList<Integer>();
				for(int i=0;i<getNoOfLayers()-1;i++){
					minWList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
//					ind= str1.indexOf(" ");
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				minWList.add(Integer.parseInt(str1));
				setMinimumWidth(minWList);
			}
			else if (strLine.contains("minimum spacing")) {////SETTING THE MINIMUM SPACING PER LAYER
				int ind=15;
				String str1 =strLine.substring(ind).trim();
				List<Integer> minSList = new ArrayList<Integer>();
				for(int i=0;i<getNoOfLayers()-1;i++){
					minSList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
//					ind= str1.indexOf(" ");
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				minSList.add(Integer.parseInt(str1));
				setMinimumSpacing(minSList);
			}
			else if (strLine.contains("via spacing")) {////SETTING THE VIA SPACING PER LAYER
				int ind=11;
				String str1 =strLine.substring(ind).trim();
				List<Integer> viaSList = new ArrayList<Integer>();
				for(int i=0;i<getNoOfLayers()-1;i++){
					viaSList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
				//	ind= ;
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				viaSList.add(Integer.parseInt(str1));
				setViaSpacingList(viaSList);
			}
			else if (strLine.contains("num net")) {////SETTING THE TOTAL NUMBER OF NETS IN THE NETLIST
				setTotalNumberOfNet(Long.parseLong(strLine.substring(7).trim()));
			}
			else if(strLine.startsWith("n")){//SETTING THE NAME, ID, NUMBER OF PINS AND MINIMUM WIDTH OF THE NETS
				int ind= strLine.indexOf(" ");
				String str1 =strLine.substring(ind).trim();
				List<Integer> nList = new ArrayList<Integer>();
				for(int i=0;i<2;i++){
					nList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
//					ind= str1.indexOf(" ");
					str1 =str1.substring(str1.indexOf(" ")).trim();
				}
				if(str1.contains("[") || str1.contains("@") || str1.contains(":"))//SOME EXTRA INFORMATION NEEDED TO ADD
					nList.add(Integer.parseInt(str1.substring(0,str1.indexOf(" "))));
				else
					nList.add(Integer.parseInt(str1));
				totalNetInformation.add(nList);
				if(oneNetList.size()>0)
					totalNetList.add(oneNetList);
				oneNetList = new ArrayList<List<Integer>>();
				prevLineCount= lineCount;
			}else if(lineCount==7){//SETTING THE LOWER LEFT CO ORDINATE OF THE LAYOUT AND THE GRID WIDTH AND HEIGHT
				setLowerLeftXCoordinate(Integer.parseInt(strLine.substring(0,strLine.indexOf(" ")).trim()));
				
				String str1 =strLine.substring(strLine.indexOf(" ")+1).trim();
				setLowerLeftYCoordinate(Integer.parseInt(str1.substring(0,str1.indexOf(" ")).trim()));
				
				int ind2= str1.indexOf(" ");
				String str2 =str1.substring(ind2).trim();
				setGridWidth(Integer.parseInt(str2.substring(0,str2.indexOf(" ")).trim()));
				
				int ind3= str2.indexOf(" ");
				String str3 =str2.substring(ind3).trim();
				setGridHeight(Integer.parseInt(str3.substring(0).trim()));
			}else if(lineCount>10 && !strLine.equals("") && (strLine.contains(" ")|| strLine.contains("\t"))){
				List<Integer> nlList = new ArrayList<Integer>();
				String str1 =strLine;
				int sp_ind=100;
				
//				System.out.println(str1);
				while(!str1.equals("")){
//					int space_ind=sp_ind;
//					int tab_ind=sp_ind;
					if(str1.contains("\t"))
						sp_ind=str1.indexOf("\t");
					if(str1.contains(" ")){
						if(str1.indexOf(" ")<sp_ind)
							sp_ind=str1.indexOf(" ");
					}
					if(!(str1.contains("\t")||str1.contains(" "))) 
						sp_ind=str1.length();
//					System.out.println("str1:"+str1+"...");
//					System.out.println(sp_ind);
//					System.out.println("to be inserted:"+str1.substring(0,sp_ind));
					nlList.add(Integer.parseInt(str1.substring(0,sp_ind)));					
					str1=str1.substring(sp_ind).trim();
				}
				if(nlList.size()>3){//STORING THE CONGESTION MATRIX
//					System.out.println("Entering in size> 3");
					capacityAdjustmentList.add(nlList);
				}
				else if(nlList.size()==3){//STORING THE NETLIST
					if(lineCount-prevLineCount==1){
						oneNetList.add(nlList);
					}						
					prevLineCount= lineCount;
				}
					
			}else if(lineCount>10 && !strLine.equals("") && !(strLine.contains(" ")|| strLine.contains("\t"))){
		//		System.out.println("****************************");
				setNumberOfCapacityAdjustment(Integer.parseInt(strLine));
				totalNetList.add(oneNetList);
			}
			lineCount++;
		}
		in.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
				
	}
	
	
	// AFTER PALCEMENT AND BEFORE THE STARTING OF ROUTING THE PRE ROUTING GUIDING INFORMATION(THE SENSITIVITY RATIO AND CONGESTION RATIO INFORMATION) FOR EACH SUBREGION IS GENERATED
			public void generatePreRoutingGuidingInformation(){
				System.out.println("Total Width in X Dir:"+getNoOfGridInXDir().intValue()*getGridWidth().intValue());
				System.out.println("Total Width in Y Dir:"+getNoOfGridInYDir().intValue()*getGridHeight().intValue());
//				Integer oneSubrXCordinate=0;
//				Integer oneSubrYCordinate=0;
				subregionInfoList = new ArrayList<List<Float>>();
				subregionMap = new HashMap<String, Float>();
				List<Float> tempList;
				
				Random randomGenerator = new Random();
				float randomCongestionRatio ;
				float randomSensitivityRatio ;
				Object[] ineligObjectArray = null;
				DecimalFormat df = new DecimalFormat("#.##");
				Float ineligFloatVal;
				
				try{
					ruleBaseClass = new TwoVarRuleFisClass();
					for(int indXCordinate=0; indXCordinate<getNoOfGridInXDir().intValue();indXCordinate++){
						for(int indYCordinate=0; indYCordinate<getNoOfGridInYDir().intValue();indYCordinate++){
							//FOR EACH SUBREGION or GRID
							tempList= new ArrayList<Float>();
							
							randomCongestionRatio = (float) (0+randomGenerator.nextDouble()*(0.5 - 0.0)); // taking the initial congestion ratio in between 0.0 - 0.5
							randomSensitivityRatio = (float) (0+randomGenerator.nextDouble()*(1.0 - 0.0)); // taking the initial sensitivity information ratio in between 0.0 - 1.0
							
							double avgSens =Double.parseDouble(df.format(randomSensitivityRatio));
							double avgCong = Double.parseDouble(df.format(randomCongestionRatio));
							
							ineligObjectArray = ruleBaseClass.TwoVarRuleFis(1, avgSens, avgCong);
							ineligFloatVal = Float.parseFloat(ineligObjectArray[0].toString());
//							System.out.println("The ineligibility factors:"+ineligObjectArray[0]);
							
							tempList.add((float)indXCordinate);//stores the row number of the sub-region: 0th element
							tempList.add((float)indYCordinate);//stores the column number of the sub-region: 1st element
							tempList.add((float)(getLowerLeftXCoordinate()+indXCordinate*getGridWidth()));//stores the X coordinate of the sub-region: 2nd element
							tempList.add((float)(getLowerLeftYCoordinate()+indYCordinate*getGridHeight()));//stores the Y coordinate of the sub-region: 3rd element
							tempList.add((float) 1); // stores Leyer Index as 1 : 4th element
							tempList.add(randomSensitivityRatio);// stores the average sensitivity value: 5th element
							tempList.add(randomCongestionRatio);// stores the average congestion ratio value: 6th element
							tempList.add(ineligFloatVal);// stores the ineligibility factor value: 7th element
							////////////////////////////////////////////////////////////
		//					if(avgSensitivityInfoForOneSR>=1){//************PROBLEM - THIS PROBLEM WILL BE SOLVED WHEN THE OVERLAP ELIMINATION ALGO WILL BE IMPLEMENTED IN THE PLACEMENT TOOL
		//						tmpCt++;
		//					}
							String subrKey = "x"+String.valueOf(indXCordinate)+"y"+String.valueOf(indXCordinate);
	//						System.out.println("subrKey..."+subrKey);
							subregionMap.put(subrKey,ineligFloatVal);
							subregionInfoList.add(tempList);
						}
						
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				System.out.println("The subregion size list size:"+subregionInfoList.size());
				
			}
			
			//RECOGNISING THE LARGE NETS WITH MULTIPLE PINS ACCROSS MULTIPLE LAYERS
			public void recogniseMultiANDTwoPinInterLayerNets(){
				multiplePinNetlistInformationList = new ArrayList<List<List<Integer>>>();
				twoPinNetlistInformationList = new ArrayList<List<List<Integer>>>();
//				twoPinInterLayerNetlistInformationList = new ArrayList<List<List<String>>>();
//				int ctTwoPin=0;
//				int ctMultiPin=0;
				for(int i=0;i<getTotalNetList().size();i++){
					if(getTotalNetList().get(i).size()==2){
//						ctTwoPin++;
						twoPinNetlistInformationList.add(totalNetList.get(i));
					}else{
//						ctMultiPin++;
						multiplePinNetlistInformationList.add(totalNetList.get(i));
					}
				}
				System.out.println("TOTAL NUMBER OF TWO PIN NETS ARE :"+twoPinNetlistInformationList.size());
				System.out.println("THE TOTAL NUMBER OF MULTI PIN NETS ARE: "+multiplePinNetlistInformationList.size());
		
			}
			
			//PERFORMING TWO PIN NETS ROUTING 
			public void performTwoPinNetsRouting() {
				try{
					int numberOfRoutingPerformed=0;
					int counter =0;
					
					for(int i=0;i<twoPinNetlistInformationList.size();i++){
					
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1)); // After each net routing,this amount of ineligibility factors will be added to those subregions, on which the path goes through
						//***********PROBLEM- HERE NOT FOR ALL NODES OF ONE NET THE SENSITIVITY INFORMATION IS NOT FOUND
							float srcXCoordinate=(float)0.0;
							float srcYCoordinate=(float)0.0;
							int srcSubRegionXCoordinate = 0;
							int srcSubRegionYCoordinate = 0;
							
							float destXCoordinate=(float)0.0;
							float destYCoordinate=(float)0.0;
							int destSubRegionXCoordinate = 0;
							int destSubRegionYCoordinate = 0;
							float layerInd = (float) 0.0;
							srcXCoordinate= ((float)twoPinNetlistInformationList.get(i).get(0).get(0));
							srcYCoordinate= ((float)twoPinNetlistInformationList.get(i).get(0).get(1));
							srcSubRegionXCoordinate = (int)srcXCoordinate/(int)getGridWidth();
							srcSubRegionYCoordinate = (int)srcYCoordinate/(int)getGridHeight();
							
							destXCoordinate= ((float)twoPinNetlistInformationList.get(i).get(1).get(0));
							destYCoordinate= ((float)twoPinNetlistInformationList.get(i).get(1).get(1));
							destSubRegionXCoordinate = (int)destXCoordinate/(int)getGridWidth();
							destSubRegionYCoordinate = (int)destYCoordinate/(int)getGridHeight();
							
//							layerInd = placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(0)).get(7);
							layerInd= 1;
							if(srcSubRegionXCoordinate<getNoOfGridInXDir() && srcSubRegionYCoordinate<getNoOfGridInYDir() && destSubRegionXCoordinate<getNoOfGridInXDir() && destSubRegionYCoordinate<getNoOfGridInYDir()){
								if(twoTerminalRoutingPathTwoVar(srcSubRegionXCoordinate,srcSubRegionYCoordinate,destSubRegionXCoordinate,destSubRegionYCoordinate,(int) layerInd))
									numberOfRoutingPerformed++;
								else
									notRoutedCount++;
//									notRoutedFlag=false;
									
								counter++;
								}
							
					}
					System.out.println("\nFOR TWO PIN NETS ROUTING PERFORMED: "+numberOfRoutingPerformed+" OUT OF "+counter+" percentage "+(((float)numberOfRoutingPerformed*100/(float)counter))+"%");
					System.out.println("ROUTING NOT PERFORMED FOR TWO PIN NETS: "+notRoutedCount+" OUT OF "+twoPinNetlistInformationList.size()+" PERCENTAGE: "+ ((float)notRoutedCount*100/(float)twoPinNetlistInformationList.size())+"%");
					System.out.println("The TOTAL WIRELENGTH FOR ALL TWO PIN NETS:"+totalWireLength);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
			//PERFORMING MULTI PIN NETS ROUTING
			public void performMultiPinNetsRouting() {
				try{
					int numberOfRoutingPerformed=0;
					int counter =0;
					int notRoutedCountHere = 0;
					for(int i=0;i<multiplePinNetlistInformationList.size();i++){
						if(multiplePinNetlistInformationList.get(i).size()<50){//Net with more than 50 terminals are left for manual routing
						float backboneXCoordinate=(float)0.0;
						float backboneYCoordinate=(float)0.0;
						int backboneSubRegionXCoordinate = 0;
						int backboneSubRegionYCoordinate =0;
						float totalSenstivityOfAllTerminalsInNet = (float) 0.0;
						float sensitivityOfOneTerminal = (float) 0.0;
						int ct=0;
						boolean notRoutedFlag =true;
						
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1)); // After each net routing,this amount of ineligibility factors will be added to those subregions, on which the path goes through
						//BACKBONE TREE CONSTRUCTION AND GETTING THE TERMINAL OF THE PSEUDO TERMINAL X AND Y COORDINATE
						//***********PROBLEM- HERE NOT FOR ALL NODES OF ONE NET THE SENSITIVITY INFORMATION IS NOT FOUND
						List<List<Integer>> allSubRegionInforPerNetToRouteList = new ArrayList<List<Integer>>();
						List<Integer> oneSubRegionInforPerNetToRouteList;
						for(int j=0;j<multiplePinNetlistInformationList.get(i).size();j++){
							float terminalXCoordinate=(float)0.0;
							float terminalYCoordinate=(float)0.0;
							int subRegionXCoordinate = 0;
							int subRegionYCoordinate = 0;
							float layerInd = (float) 0.0;
							oneSubRegionInforPerNetToRouteList = new ArrayList<Integer>();
							terminalXCoordinate= (float)(multiplePinNetlistInformationList.get(i).get(j).get(0));
							terminalYCoordinate= (float)(multiplePinNetlistInformationList.get(i).get(j).get(1));
							subRegionXCoordinate = (int)terminalXCoordinate/(int)getGridWidth();
							subRegionYCoordinate = (int)terminalYCoordinate/(int)getGridHeight();
							layerInd=(float) 1.0;
//							System.out.println("subRegionXCoordinate..."+subRegionXCoordinate+".......subRegionYCoordinate..."+subRegionYCoordinate+"layerInd...."+layerInd);
//							System.out.println("The node:"+multiplePinNetlistInformationList.get(i).get(j).get(0));
							oneSubRegionInforPerNetToRouteList.add(subRegionXCoordinate);
							oneSubRegionInforPerNetToRouteList.add(subRegionYCoordinate);
							oneSubRegionInforPerNetToRouteList.add((int) layerInd);
							
							allSubRegionInforPerNetToRouteList.add(oneSubRegionInforPerNetToRouteList);
							
							//CALCULATING THE X COORDINATE AND Y COORDINATE OF THE BACKBONE TREE FOR A MULTI TERMINAL NET
							for(int sInd=0;sInd<subregionInfoList.size();sInd++){
//								System.out.println(subRegionXCoordinate+" "+subregionInfoList.get(sInd).get(1)+":"+subRegionYCoordinate+" "+subregionInfoList.get(sInd).get(0)+":"+layerInd+" "+subregionInfoList.get(sInd).get(4));
								if((float)subRegionXCoordinate==subregionInfoList.get(sInd).get(1) && subRegionYCoordinate==subregionInfoList.get(sInd).get(0) && layerInd == subregionInfoList.get(sInd).get(4)){
									totalSenstivityOfAllTerminalsInNet+= subregionInfoList.get(sInd).get(7);
									sensitivityOfOneTerminal = subregionInfoList.get(sInd).get(7);
//									System.out.println("*********************************");
//									System.out.println("Matched "+multiplePinNetlistInformationList.get(i).get(j).get(0));
									ct++;
									break;
								}
							}
							
							backboneXCoordinate+=terminalXCoordinate *sensitivityOfOneTerminal;
							backboneYCoordinate+=terminalYCoordinate *sensitivityOfOneTerminal;
						}
						backboneXCoordinate=backboneXCoordinate/totalSenstivityOfAllTerminalsInNet;
						backboneYCoordinate=backboneYCoordinate/totalSenstivityOfAllTerminalsInNet;
						backboneSubRegionXCoordinate = (int) (backboneXCoordinate/(int)getGridWidth());
						backboneSubRegionYCoordinate = (int) (backboneYCoordinate/(int)getGridHeight());
//						System.out.println("THE TESTING"+ct+" "+multiplePinNetlistInformationList.get(i).size());
//						System.out.println("backboneXCoordinate..."+backboneXCoordinate+".......backboneYCoordinate..."+backboneYCoordinate+"\n");
//						System.out.println("backboneSubRegionXCoordinate..."+backboneSubRegionXCoordinate+".......backboneSubRegionYCoordinate..."+backboneSubRegionYCoordinate+"\n");
						
						
						for(List<Integer> oneTerminal: allSubRegionInforPerNetToRouteList){
//							System.out.println("STARTING TWO PIN NET ROUTING ..");
							if(backboneSubRegionXCoordinate<getNoOfGridInXDir() && backboneSubRegionYCoordinate<getNoOfGridInYDir() && oneTerminal.get(0)<getNoOfGridInXDir() && oneTerminal.get(1)<getNoOfGridInYDir()){
								if(twoTerminalRoutingPathTwoVar(backboneSubRegionXCoordinate,backboneSubRegionYCoordinate,oneTerminal.get(0),oneTerminal.get(1),oneTerminal.get(2)))
									numberOfRoutingPerformed++;
								else
									notRoutedFlag=false;
								counter++;
								}
							
//							System.out.println("ENDING TWO PIN NET ROUTING .."+counter);
						}
						if(!notRoutedFlag){
							notRoutedCount++;
							notRoutedCountHere++;
						}
					}
					}
//					System.out.println("numberOfRoutingPerformed........"+numberOfRoutingPerformed+" for "+counter+" percentage "+(((float)numberOfRoutingPerformed*100/(float)counter))+"%");
					System.out.println("\nROUTING NOT PERFORMED FOR MULTI PIN NETS: "+notRoutedCountHere+" OUT OF "+multiplePinNetlistInformationList.size()+" PERCENTAGE:"+ ((float)notRoutedCountHere*100/(float)multiplePinNetlistInformationList.size())+"%");
					float percentageRoutable =((float)100.0-((float)notRoutedCount*100/(float)(multiplePinNetlistInformationList.size()+twoPinNetlistInformationList.size()))) ;
					System.out.println("\nACTUAL OVER ALL(INTER+INTRA) ROUTING PERFORMED : "+(multiplePinNetlistInformationList.size()+twoPinNetlistInformationList.size()-notRoutedCount)+" OUT OF "+(multiplePinNetlistInformationList.size()+twoPinNetlistInformationList.size())
							+" PERCENTAGE: "+ percentageRoutable +"%");
					
					System.out.println("The total Wirelength:"+totalWireLength);
					System.out.println("The total number of Nets are: "+ getTotalNumberOfNet());
					System.out.println("The average Wirelength per Net: "+(float)((float)totalWireLength/(float)(multiplePinNetlistInformationList.size()+twoPinNetlistInformationList.size()-notRoutedCount)));
					System.out.println("The ratio of wirelength per miximum Manhattan Distance:"+(float)((float)((totalWireLength/(multiplePinNetlistInformationList.size()+twoPinNetlistInformationList.size()-notRoutedCount))/(float)(getNoOfGridInXDir()+getNoOfGridInYDir()))));

				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			
					
				//Returns the routing path of the two pin connection and adjusts the ineligibility factors accordingly with 2 constraints
				private boolean twoTerminalRoutingPathTwoVar(int srcSubRegionXCoordinate,int srcSubRegionYCoordinate,int destSubRegionXCoordinate,int destSubRegionYCoordinate,int layerIndex){
					
					Map<String, Float> tempSubregionMap = new HashMap<String, Float>();
					tempSubregionMap.putAll(subregionMap);
					int favouredSubRegionXCoordinate =srcSubRegionXCoordinate;
					int favouredSubRegionYCoordinate =srcSubRegionYCoordinate;
					
					//SETTING THE BOUNDARY BOX - NOT ALLOWING THE DETOUR
					int boundaryBoxXMinCoordinate;//including
					int boundaryBoxYMinCoordinate;//including
					int boundaryBoxXMaxCoordinate;//excluding
					int boundaryBoxYMaxCoordinate;//excluding
					if(srcSubRegionYCoordinate==destSubRegionYCoordinate){//Allowing maximum 1 sub region detour in both the directions
						boundaryBoxXMinCoordinate = (srcSubRegionXCoordinate<destSubRegionXCoordinate)?srcSubRegionXCoordinate:destSubRegionXCoordinate;
						boundaryBoxYMinCoordinate = (srcSubRegionYCoordinate>0)?srcSubRegionYCoordinate-1:srcSubRegionYCoordinate;
						boundaryBoxXMaxCoordinate = (srcSubRegionXCoordinate<destSubRegionXCoordinate)?(destSubRegionXCoordinate+1):(srcSubRegionXCoordinate+1);
						boundaryBoxYMaxCoordinate = (srcSubRegionYCoordinate<getNoOfGridInYDir())?(srcSubRegionYCoordinate+2):(srcSubRegionYCoordinate+2);
					}else if(srcSubRegionXCoordinate==destSubRegionXCoordinate){//Allowing maximum 1 sub region detour in both the directions
						boundaryBoxXMinCoordinate = (srcSubRegionXCoordinate>0)?srcSubRegionXCoordinate-1:srcSubRegionXCoordinate;
						boundaryBoxYMinCoordinate = (srcSubRegionYCoordinate<destSubRegionYCoordinate)?srcSubRegionYCoordinate:destSubRegionYCoordinate;
						boundaryBoxXMaxCoordinate = (srcSubRegionXCoordinate<getNoOfGridInXDir())?(srcSubRegionXCoordinate+2):(srcSubRegionXCoordinate+1);
						boundaryBoxYMaxCoordinate = (srcSubRegionYCoordinate>destSubRegionYCoordinate)?(srcSubRegionYCoordinate+1):(destSubRegionYCoordinate+1);
						
					}else{//Not allowing detour
						boundaryBoxXMinCoordinate = (srcSubRegionXCoordinate<destSubRegionXCoordinate)?srcSubRegionXCoordinate:destSubRegionXCoordinate;
						boundaryBoxYMinCoordinate = (srcSubRegionYCoordinate<destSubRegionYCoordinate)?srcSubRegionYCoordinate:destSubRegionYCoordinate;
						boundaryBoxXMaxCoordinate = (srcSubRegionXCoordinate>destSubRegionXCoordinate)?(srcSubRegionXCoordinate+1):(destSubRegionXCoordinate+1);
						boundaryBoxYMaxCoordinate = (srcSubRegionYCoordinate>destSubRegionYCoordinate)?(srcSubRegionYCoordinate+1):(destSubRegionYCoordinate+1);
					}
					
					int neighBottomSubRegionXCoordinate =srcSubRegionXCoordinate;
					int neighBottomSubRegionYCoordinate =(srcSubRegionYCoordinate-1)<boundaryBoxYMinCoordinate?-1:(srcSubRegionYCoordinate-1);
					int neighBottomSubRegionMD = Math.abs(destSubRegionXCoordinate-neighBottomSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighBottomSubRegionYCoordinate);
					
					int neighUpSubRegionXCoordinate =srcSubRegionXCoordinate;
					int neighUpSubRegionYCoordinate =(srcSubRegionYCoordinate+1)>=boundaryBoxYMaxCoordinate?-1:(srcSubRegionYCoordinate+1);
					int neighUpSubRegionMD = Math.abs(destSubRegionXCoordinate-neighUpSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighUpSubRegionYCoordinate);
					
					int neighLeftSubRegionXCoordinate =(srcSubRegionXCoordinate-1)<boundaryBoxXMinCoordinate?-1:(srcSubRegionXCoordinate-1);
					int neighLeftSubRegionYCoordinate =srcSubRegionYCoordinate;
					int neighLeftSubRegionMD = Math.abs(destSubRegionXCoordinate-neighLeftSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighLeftSubRegionYCoordinate);
					
					int neighRightSubRegionXCoordinate =(srcSubRegionXCoordinate+1)>=boundaryBoxXMaxCoordinate?-1:(srcSubRegionXCoordinate+1);
					int neighRightSubRegionYCoordinate =srcSubRegionYCoordinate;
					int neighRightSubRegionMD = Math.abs(destSubRegionXCoordinate-neighRightSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighRightSubRegionYCoordinate);
							
					int manhattanDistance = Math.abs(srcSubRegionXCoordinate-destSubRegionXCoordinate) + Math.abs(srcSubRegionYCoordinate-destSubRegionYCoordinate);
					int manhattanDistanceMax = Math.abs(srcSubRegionXCoordinate-destSubRegionXCoordinate) + Math.abs(srcSubRegionYCoordinate-destSubRegionYCoordinate);
					String strKey= "x"+String.valueOf(srcSubRegionXCoordinate)+"y"+String.valueOf(srcSubRegionYCoordinate);
//					System.out.println("THE IN ELIG FACTOR:"+subregionMap.get(strKey));
//					List<Float> ineligList;
					Float minVal = (float) 1.0;
					Float minValToAdd = minVal;
//					System.out.println("at starting...manhattanDistance.."+manhattanDistance);
//					System.out.println("TOTAL SUBREGION IN X AND Y DIRECTIONS:"+noOfSubregionInXDirection+" "+noOfSubregionInYDirection);
					
				
//					if(srcSubRegionXCoordinate<destSubRegionXCoordinate&&srcSubRegionYCoordinate>destSubRegionYCoordinate){
//						boundaryBoxXMinCoordinate=srcSubRegionXCoordinate;
//						boundaryBoxYMinCoordinate = destSubRegionYCoordinate;
//						boundaryBoxXMaxCoordinate = destSubRegionXCoordinate+1;
//						boundaryBoxYMaxCoordinate = srcSubRegionYCoordinate+1;
//					}
//						
					int countRoutingPathRepeated = 0;
					
					while(manhattanDistance>0){
						if(countRoutingPathRepeated>2*manhattanDistanceMax){
							return false;
							
						}
						else{	
							if(manhattanDistance==1){
								String favouredKeyStr;
								if(Math.abs(destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1){//For Left or right neighbour
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
								}else//For Bottom and Upper neighbour
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
								favouredSubRegionXCoordinate = destSubRegionXCoordinate;
								favouredSubRegionYCoordinate = destSubRegionYCoordinate;
								manhattanDistance = 0;
								favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
								minValToAdd = subregionMap.get(favouredKeyStr);
//								subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
								subregionMap.put(favouredKeyStr, minValToAdd);
//								System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
							}
							else if(manhattanDistance==2){//HERE EIGHT SITUATIONS MAY HAPPEN
								String favouredKeyStr;
								neighBottomSubRegionXCoordinate =favouredSubRegionXCoordinate;
								neighBottomSubRegionYCoordinate =(favouredSubRegionYCoordinate-1)<boundaryBoxYMinCoordinate?-1:(favouredSubRegionYCoordinate-1);
								neighBottomSubRegionMD = Math.abs(destSubRegionXCoordinate-neighBottomSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighBottomSubRegionYCoordinate);
								
								neighUpSubRegionXCoordinate =favouredSubRegionXCoordinate;
								neighUpSubRegionYCoordinate =(favouredSubRegionYCoordinate+1)>=boundaryBoxYMaxCoordinate?-1:(favouredSubRegionYCoordinate+1);
								neighUpSubRegionMD = Math.abs(destSubRegionXCoordinate-neighUpSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighUpSubRegionYCoordinate);
								
								neighLeftSubRegionXCoordinate =(favouredSubRegionXCoordinate-1)<boundaryBoxXMinCoordinate?-1:(favouredSubRegionXCoordinate-1);
								neighLeftSubRegionYCoordinate =favouredSubRegionYCoordinate;
								neighLeftSubRegionMD = Math.abs(destSubRegionXCoordinate-neighLeftSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighLeftSubRegionYCoordinate);
								
								neighRightSubRegionXCoordinate =(favouredSubRegionXCoordinate+1)>=boundaryBoxXMaxCoordinate?-1:(favouredSubRegionXCoordinate+1);
								neighRightSubRegionYCoordinate =favouredSubRegionYCoordinate;
								neighRightSubRegionMD = Math.abs(destSubRegionXCoordinate-neighRightSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighRightSubRegionYCoordinate);
								
								minVal = (float) 1.0;
								String strKeyBottom= "x"+String.valueOf(neighBottomSubRegionXCoordinate)+"y"+String.valueOf(neighBottomSubRegionYCoordinate);
								String strKeyUp= "x"+String.valueOf(neighUpSubRegionXCoordinate)+"y"+String.valueOf(neighUpSubRegionYCoordinate);
								String strKeyLeft= "x"+String.valueOf(neighLeftSubRegionXCoordinate)+"y"+String.valueOf(neighLeftSubRegionYCoordinate);
								String strKeyRight= "x"+String.valueOf(neighRightSubRegionXCoordinate)+"y"+String.valueOf(neighRightSubRegionYCoordinate);
								
								if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==2){//For Right neighbour
									favouredSubRegionXCoordinate =neighRightSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
								}else if((destSubRegionYCoordinate-favouredSubRegionYCoordinate)==2){//For Upper neighbour
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
									favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
								}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-2){//For Left neighbour
									favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
								}else if((destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-2){//For Bottom neighbour
									incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
									favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
								}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==1){//For Right-up neighbour
								
									// Take the Right or Up subregion with minimum ineligibility factor
									if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
										float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
										ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//										if(ineligFactForRightNeigh>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//										System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
										if(ineligFactForRightNeigh<minVal){
											favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyRight);
											minVal= ineligFactForRightNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if444");
										}
									}
									if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
										float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
										ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//										if(neighUpSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
										if(ineligFactForUpNeigh<minVal){
											favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyUp);
											minVal= ineligFactForUpNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if2222");
										}
									}
									
									
								}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==1){//For Left-up neighbour
									
									// Take the Left or Up subregion with minimum ineligibility factor
									if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
										float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
										ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//										if(neighLeftSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
										if(ineligFactForLeftNeigh<minVal){
											favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyLeft);
											minVal= ineligFactForLeftNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if333");
										}
									}
									if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
										float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
										ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//										if(neighUpSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
										if(ineligFactForUpNeigh<minVal){
											favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyUp);
											minVal= ineligFactForUpNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if2222");
										}
									}
									
								}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-1){//For Left-bottom neighbour
									
									// Take the Left or Bottom subregion with minimum ineligibility factor
									if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
										float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
										ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//										if(neighLeftSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
										if(ineligFactForLeftNeigh<minVal){
											favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyLeft);
											minVal= ineligFactForLeftNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if333");
										}
									}
									if(neighBottomSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyBottom)!=null){
										float ineligFactForBottomNeigh=((neighBottomSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyBottom)+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyBottom));
										ineligFactForBottomNeigh = (float) ((ineligFactForBottomNeigh>=1.0)?1.0:ineligFactForBottomNeigh);
//										if(neighBottomSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
										if(ineligFactForBottomNeigh<minVal){
											favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyBottom);
											minVal = ineligFactForBottomNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if111");
										}
										
									}
									
								}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-1){//For Right-bottom neighbour
									
									// Take the Right or Bottom subregion with minimum ineligibility factor
									if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
										float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
										ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//										if(ineligFactForRightNeigh>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//										System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
										if(ineligFactForRightNeigh<minVal){
											favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyRight);
											minVal= ineligFactForRightNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if444");
										}
									}
									if(neighBottomSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyBottom)!=null){
										float ineligFactForBottomNeigh=((neighBottomSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyBottom)+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyBottom));
										ineligFactForBottomNeigh = (float) ((ineligFactForBottomNeigh>=1.0)?1.0:ineligFactForBottomNeigh);
//										if(neighBottomSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
										if(ineligFactForBottomNeigh<minVal){
											favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyBottom);
											minVal = ineligFactForBottomNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if111");
										}
										
									}
									
								}
												
								manhattanDistance = 1;
								favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
								minValToAdd = subregionMap.get(favouredKeyStr);
//								minValToAdd = minVal;
//								subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
							}
							else{ //FOR MANHATTAN DISTANCE GREATER THAN TWO
//									System.out.println("at starting.."+favouredSubRegionXCoordinate+" "+favouredSubRegionYCoordinate);
									neighBottomSubRegionXCoordinate =favouredSubRegionXCoordinate;
									neighBottomSubRegionYCoordinate =(favouredSubRegionYCoordinate-1)<boundaryBoxYMinCoordinate?-1:(favouredSubRegionYCoordinate-1);
									neighBottomSubRegionMD = Math.abs(destSubRegionXCoordinate-neighBottomSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighBottomSubRegionYCoordinate);
									
									neighUpSubRegionXCoordinate =favouredSubRegionXCoordinate;
									neighUpSubRegionYCoordinate =(favouredSubRegionYCoordinate+1)>=boundaryBoxYMaxCoordinate?-1:(favouredSubRegionYCoordinate+1);
									neighUpSubRegionMD = Math.abs(destSubRegionXCoordinate-neighUpSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighUpSubRegionYCoordinate);
									
									neighLeftSubRegionXCoordinate =(favouredSubRegionXCoordinate-1)<boundaryBoxXMinCoordinate?-1:(favouredSubRegionXCoordinate-1);
									neighLeftSubRegionYCoordinate =favouredSubRegionYCoordinate;
									neighLeftSubRegionMD = Math.abs(destSubRegionXCoordinate-neighLeftSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighLeftSubRegionYCoordinate);
									
									neighRightSubRegionXCoordinate =(favouredSubRegionXCoordinate+1)>=boundaryBoxXMaxCoordinate?-1:(favouredSubRegionXCoordinate+1);
									neighRightSubRegionYCoordinate =favouredSubRegionYCoordinate;
									neighRightSubRegionMD = Math.abs(destSubRegionXCoordinate-neighRightSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-neighRightSubRegionYCoordinate);
									
									minVal = (float) 1.0;
									String strKeyBottom= "x"+String.valueOf(neighBottomSubRegionXCoordinate)+"y"+String.valueOf(neighBottomSubRegionYCoordinate);
									String strKeyUp= "x"+String.valueOf(neighUpSubRegionXCoordinate)+"y"+String.valueOf(neighUpSubRegionYCoordinate);
									String strKeyLeft= "x"+String.valueOf(neighLeftSubRegionXCoordinate)+"y"+String.valueOf(neighLeftSubRegionYCoordinate);
									String strKeyRight= "x"+String.valueOf(neighRightSubRegionXCoordinate)+"y"+String.valueOf(neighRightSubRegionYCoordinate);
									
									if(neighBottomSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyBottom)!=null){
										float ineligFactForBottomNeigh=((neighBottomSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyBottom)+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyBottom));
										ineligFactForBottomNeigh = (float) ((ineligFactForBottomNeigh>=1.0)?1.0:ineligFactForBottomNeigh);
//										if(neighBottomSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
										if(ineligFactForBottomNeigh<minVal){
											favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyBottom);
											minVal = ineligFactForBottomNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if111");
										}
										
									}
									if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
										float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
										ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//										if(neighUpSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
										if(ineligFactForUpNeigh<minVal){
											favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyUp);
											minVal= ineligFactForUpNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getVerticalCapacity().get(getNoOfLayers()-1));
//											System.out.println("in if2222");
										}
									}
									if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
										float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
										ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//										if(neighLeftSubRegionMD>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//										System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
										if(ineligFactForLeftNeigh<minVal){
											favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyLeft);
											minVal= ineligFactForLeftNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if333");
										}
									}
									if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
										float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
										ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//										if(ineligFactForRightNeigh>manhattanDistance)
//											System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//										System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
										if(ineligFactForRightNeigh<minVal){
											favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
											favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
											minValToAdd = subregionMap.get(strKeyRight);
											minVal= ineligFactForRightNeigh;
											incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/getHorizontalCapacity().get(getNoOfLayers()-2));
//											System.out.println("in if444");
										}
									}
									manhattanDistance =  Math.abs(destSubRegionXCoordinate-favouredSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-favouredSubRegionYCoordinate);
									String favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
//									subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
								//	subregionMap.put(favouredKeyStr, minValToAdd);
									tempSubregionMap.put(favouredKeyStr, (float) 1.0);
//									System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
									if(favouredSubRegionXCoordinate==getNoOfGridInXDir()||favouredSubRegionYCoordinate==getNoOfGridInYDir())
										return false;
									
							}
//							System.out.println("at ending...manhattanDistance.."+manhattanDistance);
							countRoutingPathRepeated++;
						}
					}
//					System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
				//	System.out.println("The total number of Routing path: "+countRoutingPathRepeated);
					totalWireLength = totalWireLength+countRoutingPathRepeated;
					return true;
				}
				
	
				//WRITE INPUT INFORMATION TO ANOTHER FILE
				public void writeInpToFile(){
					try {
						String outFileName = "";
						outFileName = getInpGRFileName().substring(
								getInpGRFileName().lastIndexOf("\\") + 1);
						FileWriter fstream = new FileWriter(getInpGRFileName().substring(0,
								getInpGRFileName().lastIndexOf("."))
								+ ".out");
						BufferedWriter out = new BufferedWriter(fstream);
						out.write(getNoOfGridInXDir().intValue()+" "+getNoOfGridInYDir().intValue()+" "+getNoOfLayers().intValue()+"\n");
						for(Integer vertCap:getVerticalCapacity()){
							out.write(vertCap.intValue()+" ");
						}
						out.write("\n");
						for(Integer vertCap:getHorizontalCapacity()){
							out.write(vertCap.intValue()+" ");
						}
						out.write("\n");
						for(Integer vertCap:getMinimumSpacing()){
							out.write(vertCap.intValue()+" ");
						}
						out.write("\n");
						for(Integer vertCap:getMinimumWidth()){
							out.write(vertCap.intValue()+" ");
						}
						out.write("\n");
						for(Integer vertCap:getViaSpacingList()){
							out.write(vertCap.intValue()+" ");
						}
//						for(Integer vertCap:getVerticalCapacity()){
//							out.write(vertCap.intValue()+" ");
//						}
						out.write("\n");
						out.write(getLowerLeftXCoordinate()+" "+getLowerLeftYCoordinate()+" "+getGridWidth()+" "+getGridHeight());
						out.write("\n");
						out.write(getTotalNumberOfNet().toString()+"\n");
						
						for(List<Integer> lt: getTotalNetInformation()){
							for(Integer it:lt ){
								out.write(it+" ");
							}
							out.write("\n");
						}
					//	System.out.println("SIZE::::"+getCapacityAdjustmentList().size());
						for(List<Integer> lt: getCapacityAdjustmentList()){
							for(Integer it:lt ){
								out.write(it+" ");
							}
							out.write("\n");
						}
						
				//		System.out.println("NET LIST SIZE::::"+getTotalNetList().size());
						for(List<List<Integer>> tt: getTotalNetList()){
							for(List<Integer> lt: tt){
								for(Integer it:lt ){
									out.write(it+" ");
								}
								out.write("\n");
							}
							out.write("\n\n");
						}
						
						out.write(getNumberOfCapacityAdjustment()+"\n");
						
						
						// Close the output stream
						out.close();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			

}
