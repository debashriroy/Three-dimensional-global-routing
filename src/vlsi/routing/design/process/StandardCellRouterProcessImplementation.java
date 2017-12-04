package vlsi.routing.design.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import ThreeVarTwoPinNets.ThreeVarTwoPinNetsClass;
import TwoVarRuleFis.TwoVarRuleFisClass;

import com.mathworks.toolbox.javabuilder.*;


public class StandardCellRouterProcessImplementation {
	private String inpNETFileName;
	private String inpPLFileName;
	private String inpINFOFileName;
	
	private String inpGRFileName;
	private int totalNumberOfGRNet;
//	Integer noOfGridInXDir;
//	Integer noOfGridInYDir;
	
//	Integer lowerLeftXCoordinate;
//	Integer lowerLeftYCoordinate;
//	Integer gridWidth;
//	Integer gridHeight;
//	Integer numberOfCapacityAdjustment;
	
	private float gridSize;
	private float subregionSizeInX;
	private float subregionSizeInY;
	private Integer noOfLayers = 0;
	private Long totalNumberOfNet;
	private Long totalNumberOfPins;
	private int rowNumber;
	private int colNumber;
	private float standardHeight;
	private float totalMaxHeight;
	private float totalMaxWidth;
	private float alphaParamTwoVar = (float) 0.6;
	private float betaParamTwoVar= (float) 0.4;
	
	private float alphaParamThreeVar = (float) 0.6;
	private float betaParamThreeVar= (float) 0.2;
	private float gammaParamThreeVar= (float) 0.2;
	
	private Map<String,List<Float>> placementInformationMap;
	private List<List<String>> placementInformationList;
	private List<List<List<String>>> netlistInformationList;
	private List<List<List<String>>> multiplePinNetlistInformationList;
	private Map<String,Float> sensitivityRatioInformationMap;
	private List<Integer> netDegreeList;
	private List<List<Float>> subregionInfoList;
	private Map<String, List<Float>> subRegionInfoPerTerminal;
	private List<List<List<String>>> twoPinIntraLayerNetlistInformationList;
	private List<List<List<String>>> twoPinInterLayerNetlistInformationList;
	
	TwoVarRuleFisClass ruleBaseClass = null;
//	ThreeVarTwoPinNetsClass threeVarRuleBase = null;
	int verticalCapacity = 80;
	int horizontalCapacity = 80;
	float incrementOfIneligibilityPerNetRouting;
	int noOfSubregionInXDirection=0;
	int noOfSubregionInYDirection=0;
	Map<String, Float> subregionMap;
	Map<String, Float> subregionSensitivityMap;
	Map<String, Float> subregionCongestionMap;
	int notRoutedCount = 0;
	int totalWireLength = 0;
	
	public String getInpNETFileName() {
		return inpNETFileName;
	}

	public void setInpNETFileName(String inpNETFileName) {
		this.inpNETFileName = inpNETFileName;
	}

	public String getInpPLFileName() {
		return inpPLFileName;
	}

	public void setInpPLFileName(String inpPLFileName) {
		this.inpPLFileName = inpPLFileName;
	}
	
	public String getInpINFOFileName() {
		return inpINFOFileName;
	}

	public void setInpINFOFileName(String inpINFOFileName) {
		this.inpINFOFileName = inpINFOFileName;
	}
		
	public String getInpGRFileName() {
		return inpGRFileName;
	}

	public void setInpGRFileName(String inpGRFileName) {
		this.inpGRFileName = inpGRFileName;
	}

	public Integer getNoOfLayers() {
		return noOfLayers;
	}
	public void setNoOfLayers(Integer noOfLayers) {
		this.noOfLayers = noOfLayers;
	}
	
	public Long getTotalNumberOfNet() {
		return totalNumberOfNet;
	}
	public void setTotalNumberOfNet(Long totalNumberOfNet) {
		this.totalNumberOfNet = totalNumberOfNet;
	}
	
	public float getGridSize() {
		return gridSize;
	}

	public void setGridSize(float gridSize) {
		this.gridSize = gridSize;
	}


	public Long getTotalNumberOfPins() {
		return totalNumberOfPins;
	}

	public void setTotalNumberOfPins(Long totalNumberOfPins) {
		this.totalNumberOfPins = totalNumberOfPins;
	}

	public int getTotalNumberOfGRNet() {
		return totalNumberOfGRNet;
	}

	public void setTotalNumberOfGRNet(int totalNumberOfGRNet) {
		this.totalNumberOfGRNet = totalNumberOfGRNet;
	}

	public Map<String, List<Float>> getPlacementInformationMap() {
		return placementInformationMap;
	}

	public void setPlacementInformationMap(
			Map<String, List<Float>> placementInformationMap) {
		this.placementInformationMap = placementInformationMap;
	}

	public List<List<List<String>>> getNetlistInformationList() {
		return netlistInformationList;
	}

	public void setNetlistInformationList(List<List<List<String>>> netlistInformationList) {
		this.netlistInformationList = netlistInformationList;
	}
	public List<Integer> getNetDegreeList() {
		return netDegreeList;
	}

	public void setNetDegreeList(List<Integer> netDegreeList) {
		this.netDegreeList = netDegreeList;
	}

	public Map<String, Float> getSensitivityRatioInformationMap() {
		return sensitivityRatioInformationMap;
	}

	public void setSensitivityRatioInformationMap(
			Map<String, Float> sensitivityRatioInformationMap) {
		this.sensitivityRatioInformationMap = sensitivityRatioInformationMap;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	public float getStandardHeight() {
		return standardHeight;
	}

	public void setStandardHeight(float standardHeight) {
		this.standardHeight = standardHeight;
	}

	public float getTotalMaxHeight() {
		return totalMaxHeight;
	}

	public void setTotalMaxHeight(float totalMaxHeight) {
		this.totalMaxHeight = totalMaxHeight;
	}

	public float getTotalMaxWidth() {
		return totalMaxWidth;
	}

	public void setTotalMaxWidth(float totalMaxWidth) {
		this.totalMaxWidth = totalMaxWidth;
	}

	public List<List<List<String>>> getMultiplePinNetlistInformationList() {
		return multiplePinNetlistInformationList;
	}

	public void setMultiplePinNetlistInformationList(
			List<List<List<String>>> multiplePinNetlistInformationList) {
		this.multiplePinNetlistInformationList = multiplePinNetlistInformationList;
	}

	public float getSubregionSizeInX() {
		return subregionSizeInX;
	}

	public void setSubregionSizeInX(float subregionSizeInX) {
		this.subregionSizeInX = subregionSizeInX;
	}

	public float getSubregionSizeInY() {
		return subregionSizeInY;
	}

	public void setSubregionSizeInY(float subregionSizeInY) {
		this.subregionSizeInY = subregionSizeInY;
	}

	public List<List<Float>> getSubregionInfoList() {
		return subregionInfoList;
	}

	public void setSubregionInfoList(List<List<Float>> subregionInfoList) {
		this.subregionInfoList = subregionInfoList;
	}

	public List<List<String>> getPlacementInformationList() {
		return placementInformationList;
	}

	public Map<String, List<Float>> getSubRegionInfoPerTerminal() {
		return subRegionInfoPerTerminal;
	}

	public void setSubRegionInfoPerTerminal(
			Map<String, List<Float>> subRegionInfoPerTerminal) {
		this.subRegionInfoPerTerminal = subRegionInfoPerTerminal;
	}

	public void setPlacementInformationList(List<List<String>> placementInformationList) {
		this.placementInformationList = placementInformationList;
	}

	public int getVerticalCapacity() {
		return verticalCapacity;
	}

	public void setVerticalCapacity(int verticalCapacity) {
		this.verticalCapacity = verticalCapacity;
	}

	public int getHorizontalCapacity() {
		return horizontalCapacity;
	}

	public void setHorizontalCapacity(int horizontalCapacity) {
		this.horizontalCapacity = horizontalCapacity;
	}

	public float getAlphaParam() {
		return alphaParamTwoVar;
	}

	public void setAlphaParam(float alphaParam) {
		this.alphaParamTwoVar = alphaParam;
	}

	public float getBetaParam() {
		return betaParamTwoVar;
	}

	public void setBetaParam(float betaParam) {
		this.betaParamTwoVar = betaParam;
	}

	public float getIncrementOfIneligibilityPerNetRouting() {
		return incrementOfIneligibilityPerNetRouting;
	}

	public void setIncrementOfIneligibilityPerNetRouting(
			float incrementOfIneligibilityPerNetRouting) {
		this.incrementOfIneligibilityPerNetRouting = incrementOfIneligibilityPerNetRouting;
	}



	public List<List<List<String>>> getTwoPinIntraLayerNetlistInformationList() {
		return twoPinIntraLayerNetlistInformationList;
	}

	public void setTwoPinIntraLayerNetlistInformationList(
			List<List<List<String>>> twoPinIntraLayerNetlistInformationList) {
		this.twoPinIntraLayerNetlistInformationList = twoPinIntraLayerNetlistInformationList;
	}

	public List<List<List<String>>> getTwoPinInterLayerNetlistInformationList() {
		return twoPinInterLayerNetlistInformationList;
	}

	public void setTwoPinInterLayerNetlistInformationList(
			List<List<List<String>>> twoPinInterLayerNetlistInformationList) {
		this.twoPinInterLayerNetlistInformationList = twoPinInterLayerNetlistInformationList;
	}

	public int getTotalWireLength() {
		return totalWireLength;
	}

	public void setTotalWireLength(int totalWireLength) {
		this.totalWireLength = totalWireLength;
	}

	public void readPLFileAndPopulateParams(){
		try{
			placementInformationMap = new HashMap<String, List<Float>>();
			placementInformationList = new ArrayList<List<String>>();
		FileInputStream fstream = new FileInputStream(getInpPLFileName());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int ct=0;
		int ctn=0;
		int cts=0;
		System.out.println("The dulicate entry:");
		while ((strLine = br.readLine()) != null) {
			if(!strLine.contains("#") && !strLine.trim().equals("")){
				ct++;
				int breakInd=strLine.indexOf("\t");
				List<Float> oneList= new ArrayList<Float>();
				List<String> oneStringList = new ArrayList<String>();
				String nodeName= strLine.substring(0,breakInd);
//				if(nodeName.equals("")){
//					ctn++;
//				}
				if(!nodeName.equals("")){
					cts++;
					String str1 =  strLine.substring(breakInd).trim();
					oneStringList.add(nodeName);
					while(!str1.equals("") && str1.contains("\t")){
						breakInd=str1.indexOf("\t");
						oneList.add(Float.parseFloat(str1.substring(0,breakInd).trim()));
						oneStringList.add(str1.substring(0,breakInd).trim());
						str1 =  str1.substring(breakInd).trim();
					}
					if(!str1.equals("")){
						oneList.add(Float.parseFloat(str1.trim()));
						oneStringList.add(str1.trim());
					}
//					System.out.println("PRINTING THE FLOAT LIST:");
//					for(Float fl:oneList){
//						System.out.println(fl);
//					}
					if(placementInformationMap.containsKey(nodeName)){
						ctn++;
						System.out.println(nodeName);
					}
					else{
						placementInformationMap.put(nodeName, oneList);
						placementInformationList.add(oneStringList);
					}
//					System.out.println("Entering");
				}
			}
		}
		System.out.println("The number of placemented Nodes are: "+placementInformationMap.size()+" "+ct+" "+cts+" "+ctn);
		br.close();
		in.close();
		fstream.close();
//		System.out.println("ct.."+ct+" "+ctn+" "+cts+" "+placementInformationMap.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readNETSFileAndPopulateParams(){
		try{
			netlistInformationList = new ArrayList<List<List<String>>>();
			netDegreeList = new ArrayList<Integer>();
			List<List<String>> oneNetList =  new ArrayList<List<String>>();
			FileInputStream fstream = new FileInputStream(getInpNETFileName());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if(!strLine.contains("#") && !strLine.trim().equals("") && !strLine.contains("UCLA")){
					int brkIndex=0;
					if(strLine.contains("NumNets")){
						brkIndex=strLine.indexOf(" ");
						if(strLine.indexOf("\t")>brkIndex)
							brkIndex= strLine.indexOf("\t");
						setTotalNumberOfNet(Long.parseLong(strLine.substring(brkIndex).trim()));
//						System.out.println("Numnets:"+Long.parseLong(strLine.substring(brkIndex).trim()));
					}					
					else if(strLine.contains("NumPins")){
						brkIndex=strLine.indexOf(" ");
						if(strLine.indexOf("\t")>brkIndex)
							brkIndex= strLine.indexOf("\t");
						setTotalNumberOfPins(Long.parseLong(strLine.substring(brkIndex).trim()));
//						System.out.println("NumPins:"+Long.parseLong(strLine.substring(brkIndex).trim()));
					}else if(strLine.contains("NetDegree")){
						
						netDegreeList.add(Integer.parseInt(strLine.substring(strLine.lastIndexOf(" ")).trim()));
						if(oneNetList.size()>0)
							netlistInformationList.add(oneNetList);
						oneNetList =  new ArrayList<List<String>>();
					}else{
						List<String> strList =  new ArrayList<String>();
						String str1="";
						if(strLine.contains("I"))
							strList.add(strLine.substring(0, strLine.indexOf("I")).trim());
						else if(strLine.contains("O"))
							strList.add(strLine.substring(0, strLine.indexOf("O")).trim());
						str1= strLine.substring(strLine.indexOf(":")+1).trim();
						strList.add( str1.substring(0,str1.indexOf(" ")).trim());
						strList.add(str1.substring(str1.indexOf(" ")).trim());
						oneNetList.add(strList);
					}
						
				}
			}
			if(oneNetList.size()>0)
				netlistInformationList.add(oneNetList);
			br.close();
			in.close();
			fstream.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readINFOFileAndPopulateParams(){
		try{
			sensitivityRatioInformationMap = new HashMap<String, Float>();
			FileInputStream fstream = new FileInputStream(getInpINFOFileName());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int MaxVal=0;
			while ((strLine = br.readLine()) != null) {
				if(!strLine.trim().equals("")){
					if(strLine.contains("#NO_OF_LAYERS")){
						setNoOfLayers(Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim()));
					}else if(strLine.contains("#MAX_VALUE")){
						MaxVal = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(strLine.contains("#ROW_NO")){
						rowNumber = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(strLine.contains("#COL_NO")){
						colNumber = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(strLine.contains("#STANDARD_HEIGHT")){
						standardHeight = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(strLine.contains("#TOTAL_MAX_WIDTH")){
						totalMaxWidth = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(strLine.contains("#TOTAL_MAX_HEIGHT")){
						totalMaxHeight = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
					}else if(!strLine.contains("#")){
					String nodeName= strLine.substring(0,strLine.indexOf("\t")).trim();
					String sensitivityInfo =  strLine.substring(strLine.indexOf("\t")).trim();
					sensitivityRatioInformationMap.put(nodeName, (Float.parseFloat(sensitivityInfo)/MaxVal));
					}
				}
			}
			
			br.close();
			in.close();
			fstream.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readGRFileAndPopulateParams(){
		try{
//			sensitivityRatioInformationMap = new HashMap<String, Float>();
			FileInputStream fstream = new FileInputStream(getInpGRFileName());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int MaxVal=0;
			while ((strLine = br.readLine()) != null) {
				MaxVal++;
				System.out.println("Entering into GRFile Reading Function");
				if(MaxVal==9){
//					System.out.println("++++:"+strLine.trim());
//					System.out.println("++++:"+strLine.trim().substring(strLine.indexOf(" "))+" "+strLine.length());
//					setTotalNumberOfGRNet(Integer.parseInt(strLine.trim().substring(strLine.indexOf(" "),strLine.length())));
					setTotalNumberOfGRNet(Integer.parseInt(strLine.trim().substring(8,strLine.length())));
					System.out.println("Number of Net:"+getTotalNumberOfGRNet());
					break;
				}
//				if(!strLine.trim().equals("")){
//					if(strLine.contains("#NO_OF_LAYERS")){
//						setNoOfLayers(Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim()));
//					}else if(strLine.contains("#MAX_VALUE")){
//						MaxVal = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(strLine.contains("#ROW_NO")){
//						rowNumber = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(strLine.contains("#COL_NO")){
//						colNumber = Integer.parseInt(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(strLine.contains("#STANDARD_HEIGHT")){
//						standardHeight = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(strLine.contains("#TOTAL_MAX_WIDTH")){
//						totalMaxWidth = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(strLine.contains("#TOTAL_MAX_HEIGHT")){
//						totalMaxHeight = Float.parseFloat(strLine.substring(strLine.indexOf("\t")).trim());
//					}else if(!strLine.contains("#")){
//					String nodeName= strLine.substring(0,strLine.indexOf("\t")).trim();
//					String sensitivityInfo =  strLine.substring(strLine.indexOf("\t")).trim();
//					sensitivityRatioInformationMap.put(nodeName, (Float.parseFloat(sensitivityInfo)/MaxVal));
//					}
//				}
			}
			
			br.close();
			in.close();
			fstream.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findNotPlacementedNode(){
		System.out.println("The Total Nodes in .INFO File"+sensitivityRatioInformationMap.size());
		System.out.println("Nodes present in .PL File: "+ placementInformationMap.size());
		System.out.println("THE NOT PRESENTED NODES ARE:");
		int ct=0;
		for(String nodeName: sensitivityRatioInformationMap.keySet()){
		//	System.out.println(nodeName);
		if(placementInformationMap.get(nodeName)==null){
			System.out.println(nodeName);
			ct++;
		}
		}
		System.out.println("The number of nodes not placed are: "+ct);
	}
	
	public void findDuplicatePlacementedNode(){
	//	System.out.println("The Total Nodes in .INFO File"+sensitivityRatioInformationMap.size());
	//	System.out.println("Nodes present in .PL File: "+ placementInformationMap.size());
		System.out.println("THE DUPLICATE PLACED NODES ARE:");
		int ct=0;
		for(String nodeName: placementInformationMap.keySet()){
		//	System.out.println(nodeName);
		if(placementInformationMap.get(nodeName)==null){
			System.out.println(nodeName);
			ct++;
		}
		}
		System.out.println("The number of nodes not placed are: "+ct);
	}
	
	
	public void findMaxWidthOfLayout(){
		float maxWidth = (float) 0.0;
		for(List<String> oneList:placementInformationList){
			if(Float.parseFloat(oneList.get(6))>maxWidth){
				maxWidth= Float.parseFloat(oneList.get(6));
			}
		}
		totalMaxWidth = maxWidth;
		System.out.println("totalMaxWidth..."+totalMaxWidth);
		int noOfSubregionInXDir =(int) (totalMaxWidth/subregionSizeInX); //////////////////////////////////
		int noOfSubregionInYDir =(int) (totalMaxHeight/subregionSizeInY);
		if((float)totalMaxWidth%(float)subregionSizeInX!=0.0)
			noOfSubregionInXDir++;
		if((float)totalMaxHeight%(float)subregionSizeInY!=0.0)
			noOfSubregionInYDir++;
		System.out.println("noOfSubregionInXDir..."+noOfSubregionInXDir);
		System.out.println("noOfSubregionInYDir......"+noOfSubregionInYDir);
	}
	public void populatePlacementInformationList(){
		
	}
	
	// AFTER PALCEMENT AND BEFORE THE STARTING OF ROUTING THE PRE ROUTING GUIDING INFORMATION(THE SENSITIVITY RATIO AND CONGESTION RATIO INFORMATION) FOR EACH SUBREGION IS GENERATED
	public void generatePreRoutingGuidingInformationForTwoVar(){
		subregionInfoList = new ArrayList<List<Float>>();
		subregionMap = new HashMap<String, Float>();
		subregionSizeInX = 2*standardHeight;
		subregionSizeInY = 2* standardHeight;
		int noOfSubregionInXDir =(int) (totalMaxWidth/subregionSizeInX); //////////////////////////////////
		int noOfSubregionInYDir =(int) (totalMaxHeight/subregionSizeInY);
		if((float)totalMaxWidth%(float)subregionSizeInX!=0.0)
			noOfSubregionInXDir++;
		if((float)totalMaxHeight%(float)subregionSizeInY!=0.0)
			noOfSubregionInYDir++;
		List<Float> tempList;
		float x1Coordinate = (float)0.0;
		float x2Coordinate = (float) 0.0;
		float y1Coordinate = (float) 0.0;
		float y2Coordinate = (float) 0.0;
		float totalSensitivityInfoForOneSR=(float) 0.0;
		float avgSensitivityInfoForOneSR = (float) 0.0;
		int tmpCt=0;
		Random randomGenerator = new Random();
		float randomCongestionRatio ;
		Object[] ineligObjectArray = null;
		DecimalFormat df = new DecimalFormat("#.##");
		Float ineligFloatVal;
		try{
			noOfSubregionInXDirection = noOfSubregionInXDir;
			noOfSubregionInYDirection = noOfSubregionInYDir;
			ruleBaseClass = new TwoVarRuleFisClass();
			//Storing the sensitivity information of each sub-regions for standard cells
			for(int layerInd = 0;layerInd<noOfLayers;layerInd++){//For each Layer
				//traversing row wise in bottom up manner, the bottom left corner (0, 0) 
				for(int rInd = 0 ; rInd<noOfSubregionInYDir;rInd++){//Row index : moving in y direction
					y1Coordinate = (float)rInd*subregionSizeInY;
					y2Coordinate = (float)rInd*subregionSizeInY+subregionSizeInY;
					for(int cInd = 0 ; cInd<noOfSubregionInXDir;cInd++){//Column index : moving in x direction
						//FOR EACH SUBREGION
						tempList= new ArrayList<Float>();
						x1Coordinate = (float)cInd*subregionSizeInX;
						x2Coordinate = (float)cInd*subregionSizeInX+subregionSizeInX;
									
						tempList.add((float)rInd);//stores the row number of the sub-region: 0th element
						tempList.add((float)cInd);//stores the column number of the sub-region: 1st element
						tempList.add(x1Coordinate);//stores the X coordinate of the sub-region: 2nd element
						tempList.add(y1Coordinate);//stores the Y coordinate of the sub-region: 3rd element
						tempList.add((float) layerInd);//stores the layer index of the sub-region: 4th element
						
						totalSensitivityInfoForOneSR=(float) 0.0;
						avgSensitivityInfoForOneSR = (float) 0.0;
						//CALCULATING THE AVERAGE SENSITIVITY FOR EACH SUB-REGION
						for(int i = 0;i<placementInformationList.size();i++){
							if(Float.parseFloat(placementInformationList.get(i).get(7))==y1Coordinate && Integer.parseInt(placementInformationList.get(i).get(8))==layerInd){
								if(x1Coordinate<=Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate>Float.parseFloat(placementInformationList.get(i).get(5))
										&& x1Coordinate<Float.parseFloat(placementInformationList.get(i).get(6)) && x2Coordinate>=Float.parseFloat(placementInformationList.get(i).get(6))){
	//										System.out.println("Entering in If111111111111111:"+placementInformationList.get(i).get(0));
											totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*Float.parseFloat(placementInformationList.get(i).get(3));
										}
								if(x1Coordinate>Float.parseFloat(placementInformationList.get(i).get(5)) 
										&& x1Coordinate<Float.parseFloat(placementInformationList.get(i).get(6)) && x2Coordinate>=Float.parseFloat(placementInformationList.get(i).get(6))){
	//										System.out.println("Entering in If2222222222:"+placementInformationList.get(i).get(0));
											totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*(Float.parseFloat(placementInformationList.get(i).get(6))-x1Coordinate);
										}
								if(x1Coordinate<=Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate>Float.parseFloat(placementInformationList.get(i).get(5))
										&& x2Coordinate<Float.parseFloat(placementInformationList.get(i).get(6))){
	//										System.out.println("Entering in If3333333333333:"+placementInformationList.get(i).get(0));
											totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*(x2Coordinate-Float.parseFloat(placementInformationList.get(i).get(5)));
										}
								if(x1Coordinate>Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate<Float.parseFloat(placementInformationList.get(i).get(6))){
	//										System.out.println("Entering in If4444444444444:"+placementInformationList.get(i).get(0));
											totalSensitivityInfoForOneSR=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*subregionSizeInX;
										}
							}
						}
						avgSensitivityInfoForOneSR = totalSensitivityInfoForOneSR/(subregionSizeInX);
	//					System.out.println("AVERAGE SENSITIVITY OF ("+cInd+","+rInd+","+noOfLayers+") is:"+avgSensitivityInfoForOneSR);
						avgSensitivityInfoForOneSR =(avgSensitivityInfoForOneSR>1)?1:avgSensitivityInfoForOneSR;//***************PROBLEM- FOR THE OVERLAPPED CELLS AT THE BOUNDARY POSITIONS THE AVG SENSITIVITY FOR THOSE SUBREGION IS >=1, SO HERE NORMALISING THEM
						randomCongestionRatio = (float) (0+randomGenerator.nextDouble()*(0.5 - 0.0)); // taking the initial congestion ratio in between 0.0 - 0.5
						double avgSens =Double.parseDouble(df.format(avgSensitivityInfoForOneSR));
						double avgCong = Double.parseDouble(df.format(randomCongestionRatio));
		//				System.out.println("randomCongestionRatio..."+avgSens+" "+avgCong);
	//					df.format(avgSensitivityInfoForOneSR)
						//////////////////////////////////////////////////////////
//						ineligObjectArray=ruleBaseClass.TwoVarRuleFis(1, 0.2, 0.8);
			//			System.out.println("The ineligibility:"+ineligObjectArray[0]);
						ineligObjectArray = ruleBaseClass.TwoVarRuleFis(1, avgSens, avgCong);
						ineligFloatVal = Float.parseFloat(ineligObjectArray[0].toString());
		//				System.out.println("The ineligibility factors:"+ineligObjectArray[0]);
						tempList.add(avgSensitivityInfoForOneSR);// stores the average sensitivity value: 5th element
						tempList.add(randomCongestionRatio);// stores the average congestion ratio value: 6th element
						tempList.add(ineligFloatVal);// stores the ineligibility factor value: 7th element
						////////////////////////////////////////////////////////////
	//					if(avgSensitivityInfoForOneSR>=1){//************PROBLEM - THIS PROBLEM WILL BE SOLVED WHEN THE OVERLAP ELIMINATION ALGO WILL BE IMPLEMENTED IN THE PLACEMENT TOOL
	//						tmpCt++;
	//					}
						String subrKey = "x"+String.valueOf((int)(x1Coordinate/subregionSizeInX))+"y"+String.valueOf((int)(y1Coordinate/subregionSizeInY));
//						System.out.println("subrKey..."+subrKey);
						subregionMap.put(subrKey,ineligFloatVal);
						subregionInfoList.add(tempList);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("The subregion size list size:"+subregionInfoList.size()+" "+tmpCt);
	//	printSubregionList();
	}
	
	//PRINTING THE TOTAL SUB REGION LIST
	public void printSubregionList(){
		System.out.println("ALL SUB REGION INFORMATION");
		for(int i=0; i<subregionInfoList.size();i++){
			System.out.print("The"+(i+1)+"th sub region information:");
			for(Float subR : subregionInfoList.get(i)){
				System.out.print(subR+" ");
			}
			System.out.println();
		}
		
	}
	
	// AFTER PALCEMENT AND BEFORE THE STARTING OF ROUTING THE PRE ROUTING GUIDING INFORMATION(THE SENSITIVITY RATIO AND CONGESTION RATIO INFORMATION AND DISTANCE RATIO) FOR EACH SUBREGION IS GENERATED
	public void generatePreRoutingGuidingInformationForThreeVar(){
			subregionInfoList = new ArrayList<List<Float>>();
			subregionSensitivityMap = new HashMap<String, Float>();
			subregionCongestionMap = new HashMap<String, Float>();
			subregionSizeInX = 2*standardHeight;
			subregionSizeInY = 2* standardHeight;
			int noOfSubregionInXDir =(int) (totalMaxWidth/subregionSizeInX); //////////////////////////////////
			int noOfSubregionInYDir =(int) (totalMaxHeight/subregionSizeInY);
			if((float)totalMaxWidth%(float)subregionSizeInX!=0.0)
				noOfSubregionInXDir++;
			if((float)totalMaxHeight%(float)subregionSizeInY!=0.0)
				noOfSubregionInYDir++;
			List<Float> tempList;
			float x1Coordinate = (float)0.0;
			float x2Coordinate = (float) 0.0;
			float y1Coordinate = (float) 0.0;
			float y2Coordinate = (float) 0.0;
			float totalSensitivityInfoForOneSR=(float) 0.0;
			float avgSensitivityInfoForOneSR = (float) 0.0;
			int tmpCt=0;
			Random randomGenerator = new Random();
			float randomCongestionRatio ;
			Object[] ineligObjectArray = null;
			DecimalFormat df = new DecimalFormat("#.##");
			Float ineligFloatVal;
			try{
				noOfSubregionInXDirection = noOfSubregionInXDir;
				noOfSubregionInYDirection = noOfSubregionInYDir;
		//		threeVarRuleBase = new ThreeVarTwoPinNetsClass();
				//Storing the sensitivity information of each sub-regions for standard cells
				for(int layerInd = 0;layerInd<noOfLayers;layerInd++){//For each Layer
					//traversing row wise in bottom up manner, the bottom left corner (0, 0) 
					for(int rInd = 0 ; rInd<noOfSubregionInYDir;rInd++){//Row index : moving in y direction
						y1Coordinate = (float)rInd*subregionSizeInY;
						y2Coordinate = (float)rInd*subregionSizeInY+subregionSizeInY;
						for(int cInd = 0 ; cInd<noOfSubregionInXDir;cInd++){//Column index : moving in x direction
							//FOR EACH SUBREGION
							tempList= new ArrayList<Float>();
							x1Coordinate = (float)cInd*subregionSizeInX;
							x2Coordinate = (float)cInd*subregionSizeInX+subregionSizeInX;
										
							tempList.add((float)rInd);//stores the row number of the sub-region: 0th element
							tempList.add((float)cInd);//stores the column number of the sub-region: 1st element
							tempList.add(x1Coordinate);//stores the X coordinate of the sub-region: 2nd element
							tempList.add(y1Coordinate);//stores the Y coordinate of the sub-region: 3rd element
							tempList.add((float) layerInd);//stores the layer index of the sub-region: 4th element
							
							totalSensitivityInfoForOneSR=(float) 0.0;
							avgSensitivityInfoForOneSR = (float) 0.0;
							//CALCULATING THE AVERAGE SENSITIVITY FOR EACH SUB-REGION
							for(int i = 0;i<placementInformationList.size();i++){
								if(Float.parseFloat(placementInformationList.get(i).get(7))==y1Coordinate && Integer.parseInt(placementInformationList.get(i).get(8))==layerInd){
									if(x1Coordinate<=Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate>Float.parseFloat(placementInformationList.get(i).get(5))
											&& x1Coordinate<Float.parseFloat(placementInformationList.get(i).get(6)) && x2Coordinate>=Float.parseFloat(placementInformationList.get(i).get(6))){
		//										System.out.println("Entering in If111111111111111:"+placementInformationList.get(i).get(0));
												totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*Float.parseFloat(placementInformationList.get(i).get(3));
											}
									if(x1Coordinate>Float.parseFloat(placementInformationList.get(i).get(5)) 
											&& x1Coordinate<Float.parseFloat(placementInformationList.get(i).get(6)) && x2Coordinate>=Float.parseFloat(placementInformationList.get(i).get(6))){
		//										System.out.println("Entering in If2222222222:"+placementInformationList.get(i).get(0));
												totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*(Float.parseFloat(placementInformationList.get(i).get(6))-x1Coordinate);
											}
									if(x1Coordinate<=Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate>Float.parseFloat(placementInformationList.get(i).get(5))
											&& x2Coordinate<Float.parseFloat(placementInformationList.get(i).get(6))){
		//										System.out.println("Entering in If3333333333333:"+placementInformationList.get(i).get(0));
												totalSensitivityInfoForOneSR+=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*(x2Coordinate-Float.parseFloat(placementInformationList.get(i).get(5)));
											}
									if(x1Coordinate>Float.parseFloat(placementInformationList.get(i).get(5)) && x2Coordinate<Float.parseFloat(placementInformationList.get(i).get(6))){
		//										System.out.println("Entering in If4444444444444:"+placementInformationList.get(i).get(0));
												totalSensitivityInfoForOneSR=sensitivityRatioInformationMap.get(placementInformationList.get(i).get(0)).floatValue()*subregionSizeInX;
											}
								}
							}
							avgSensitivityInfoForOneSR = totalSensitivityInfoForOneSR/(subregionSizeInX);
		//					System.out.println("AVERAGE SENSITIVITY OF ("+cInd+","+rInd+","+noOfLayers+") is:"+avgSensitivityInfoForOneSR);
							avgSensitivityInfoForOneSR =(avgSensitivityInfoForOneSR>1)?1:avgSensitivityInfoForOneSR;//***************PROBLEM- FOR THE OVERLAPPED CELLS AT THE BOUNDARY POSITIONS THE AVG SENSITIVITY FOR THOSE SUBREGION IS >=1, SO HERE NORMALISING THEM
							randomCongestionRatio = (float) (0+randomGenerator.nextDouble()*(0.5 - 0.0)); // taking the initial congestion ratio in between 0.0 - 0.5
							double avgSens =Double.parseDouble(df.format(avgSensitivityInfoForOneSR));
							double avgCong = Double.parseDouble(df.format(randomCongestionRatio));
			//				System.out.println("randomCongestionRatio..."+avgSens+" "+avgCong);
		//					df.format(avgSensitivityInfoForOneSR)
							//////////////////////////////////////////////////////////
//							ineligObjectArray=ruleBaseClass.TwoVarRuleFis(1, 0.2, 0.8);
				//			System.out.println("The ineligibility:"+ineligObjectArray[0]);
					//		ineligObjectArray = threeVarRuleBase.ThreeVarTwoPinNetsFis(1, avgSens, avgCong, 0.5, 2);
							ineligFloatVal = Float.parseFloat(ineligObjectArray[0].toString());
			//				System.out.println("The ineligibility factors:"+ineligObjectArray[0]);
							tempList.add(avgSensitivityInfoForOneSR);// stores the average sensitivity value: 5th element
							tempList.add(randomCongestionRatio);// stores the average congestion ratio value: 6th element
							tempList.add(ineligFloatVal);// stores the ineligibility factor value: 7th element
							////////////////////////////////////////////////////////////
		//					if(avgSensitivityInfoForOneSR>=1){//************PROBLEM - THIS PROBLEM WILL BE SOLVED WHEN THE OVERLAP ELIMINATION ALGO WILL BE IMPLEMENTED IN THE PLACEMENT TOOL
		//						tmpCt++;
		//					}
							String subrKey = "x"+String.valueOf((int)(x1Coordinate/subregionSizeInX))+"y"+String.valueOf((int)(y1Coordinate/subregionSizeInY));
//							System.out.println("subrKey..."+subrKey);
							subregionSensitivityMap.put(subrKey,avgSensitivityInfoForOneSR);
							subregionCongestionMap.put(subrKey,randomCongestionRatio);
							subregionInfoList.add(tempList);
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("The subregion size list size:"+subregionInfoList.size()+" "+tmpCt);
		//	printSubregionList();
		}
	
	//RECOGNISING THE LARGE NETS WITH MULTIPLE PINS ACCROSS MULTIPLE LAYERS
	public void recogniseMultiANDTwoPinInterLayerNets(){
		multiplePinNetlistInformationList = new ArrayList<List<List<String>>>();
		twoPinIntraLayerNetlistInformationList = new ArrayList<List<List<String>>>();
		twoPinInterLayerNetlistInformationList = new ArrayList<List<List<String>>>();
		int ct=0;
		int ct1=0;
		int ct2=0;
		int ct3 = 0;
		int ct4 =0;
		for(int i=0;i<netlistInformationList.size();i++){
			if(netlistInformationList.get(i).size()>=2){
//				System.out.println((i+1)+"THIS IS A MULTIPIN NET");
//				ct++;
				ct3++;
				boolean testMultipleLayers=true;
				int nodeCount=0;
				for(int j=0;j<netlistInformationList.get(i).size()-1;j++){
					if(placementInformationMap.get(netlistInformationList.get(i).get(j).get(0))!=null && placementInformationMap.get(netlistInformationList.get(i).get(j+1).get(0))!=null){
						nodeCount++;
//						System.out.println("The corresponding layer for "+netlistInformationList.get(i).get(j).get(0)+" is: "+placementInformationMap.get(netlistInformationList.get(i).get(j).get(0)).get(7));
						if(placementInformationMap.get(netlistInformationList.get(i).get(j).get(0)).get(7).floatValue()!=placementInformationMap.get(netlistInformationList.get(i).get(j+1).get(0)).get(7).floatValue()){
							testMultipleLayers=false;							
						}
					}
						//GETTING THE MULTIPLE PIN NETS
					
				}
//				System.out.println("NODE COUNT:"+nodeCount+" "+testMultipleLayers);
				if(nodeCount>=2)
					ct++;
				//FOR ONLY MULTI PIN INTER LAYER NETS
//				if(!testMultipleLayers && nodeCount>2){
//					multiplePinNetlistInformationList.add(netlistInformationList.get(i));
//					ct1++;
//				}
				//FOR ALL INTER/INTRA LAYER MULTI PIN AND INTER LAYER TWO PIN NETS
				if(nodeCount>=2 || (nodeCount==1 && !testMultipleLayers && netlistInformationList.get(i).size()==2)){
					multiplePinNetlistInformationList.add(netlistInformationList.get(i));
					ct1++;
				}
				// FOR TWO -PIN INTER LAYER NETS(FOR INDICON PAPER)
				else if(nodeCount==1 && !testMultipleLayers && netlistInformationList.get(i).size()==2){//Two pin inter-layer net
					twoPinInterLayerNetlistInformationList.add(netlistInformationList.get(i));
					ct2++;
				}
				//FOR ONLY TWO PIN INTRA LAYER NETS
				else if(nodeCount==1 && testMultipleLayers && netlistInformationList.get(i).size()==2){//Two pin intra-layer net
					twoPinIntraLayerNetlistInformationList.add(netlistInformationList.get(i));
					ct4++;
				}
			}
		}
		//***********need to work with interlayer two pin net
		System.out.println("THE TOTAL MULTI PIN NETS ARE: "+ct+" AND  INTER LAYER NETS ARE:"+ct1+" TWO PIN INTRA-LAYER NETS ARE"+ct4+"TOTAL NUMBER OF NETS ARE:"+ct3);
		
		System.out.println("THE INTER-LAYER TWO PIN NETS ARE"+ct2);
		removeNotPlacedNodesFromMultiplePinInterLayerNetList();
		removeNotPlacedNodesFromTwoPinIntraLayerNetList();
//		removeNotPlacedNodesFromTwoPinInterLayerNetList();//FOR INDICON PAPER
		
	}
	
	
	//THIS METHOD IS REQUIRED AS NOT ALL NODES OF .NODES FILE IS NOT BEING PLACED
	public void removeNotPlacedNodesFromMultiplePinInterLayerNetList(){
		
		for(int i=0;i<multiplePinNetlistInformationList.size();i++){
			for(int j=multiplePinNetlistInformationList.get(i).size()-1;j>=0;j--){
				if(placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0))==null){
//					System.out.println("THE VALUE.."+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)));
					multiplePinNetlistInformationList.get(i).remove(j);
					
				}
			}
			
		}
	}
		
	//THIS METHOD IS REQUIRED AS NOT ALL NODES OF .NODES FILE IS NOT BEING PLACED
	public void removeNotPlacedNodesFromTwoPinIntraLayerNetList(){
			
			for(int i=0;i<twoPinIntraLayerNetlistInformationList.size();i++){
				for(int j=twoPinIntraLayerNetlistInformationList.get(i).size()-1;j>=0;j--){
					if(placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(j).get(0))==null){
//						System.out.println("THE VALUE.."+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)));
						twoPinIntraLayerNetlistInformationList.get(i).remove(j);
						
					}
				}
				if(multiplePinNetlistInformationList.get(i).size()<2){
					System.out.println("**********"+multiplePinNetlistInformationList.get(i).size());
					
				}
			}
		}
				
	//THE THREE STEP APPROACH ALGORITHM FOR TWO PIN INTRA LAYER NETS ROUTING 
	public void performTwoPinIntraLayerNetsRouting(){
			int numberOfRoutingPerformed=0;
			int counter =0;
			
			for(int i=0;i<twoPinIntraLayerNetlistInformationList.size();i++){
//				float totalSenstivityOfAllTerminalsInNet = (float) 0.0;
//				float sensitivityOfOneTerminal = (float) 0.0;
//				int ct=0;
//				boolean notRoutedFlag =true;
				
				incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity); // After each net routing,this amount of ineligibility factors will be added to those subregions, on which the path goes through
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
					srcXCoordinate= Float.parseFloat(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(1))+placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(0)).get(4);
					srcYCoordinate= Float.parseFloat(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(2))+placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(0)).get(6);
//					System.out.println("terminal name:"+twoPinIntraLayerNetlistInformationList.get(i).get(0).get(0)+"srcXCoordinate..."+srcXCoordinate+".......srcYCoordinate..."+srcYCoordinate);
					srcSubRegionXCoordinate = (int)srcXCoordinate/(int)subregionSizeInX;
					srcSubRegionYCoordinate = (int)srcYCoordinate/(int)subregionSizeInY;
//					System.out.println("srcSubRegionXCoordinate..."+srcSubRegionXCoordinate+".......srcSubRegionYCoordinate..."+srcSubRegionYCoordinate+"layerInd...."+layerInd);
					
					destXCoordinate= Float.parseFloat(twoPinIntraLayerNetlistInformationList.get(i).get(1).get(1))+placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(1).get(0)).get(4);
					destYCoordinate= Float.parseFloat(twoPinIntraLayerNetlistInformationList.get(i).get(1).get(2))+placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(1).get(0)).get(6);
//					System.out.println("terminal name:"+twoPinIntraLayerNetlistInformationList.get(i).get(1).get(0)+"destXCoordinate..."+destXCoordinate+".......destYCoordinate..."+destYCoordinate);
					destSubRegionXCoordinate = (int)destXCoordinate/(int)subregionSizeInX;
					destSubRegionYCoordinate = (int)destYCoordinate/(int)subregionSizeInY;
//					System.out.println("destSubRegionXCoordinate..."+destSubRegionXCoordinate+".......backboneYCoordinate..."+destSubRegionYCoordinate+"\n");
					
					layerInd = placementInformationMap.get(twoPinIntraLayerNetlistInformationList.get(i).get(0).get(0)).get(7);
				
//					System.out.println("STARTING TWO PIN NET ROUTING ..");
					if(srcSubRegionXCoordinate<noOfSubregionInXDirection && srcSubRegionYCoordinate<noOfSubregionInYDirection && destSubRegionXCoordinate<noOfSubregionInXDirection && destSubRegionYCoordinate<noOfSubregionInYDirection){
						if(twoTerminalRoutingPathTwoVar(srcSubRegionXCoordinate,srcSubRegionYCoordinate,destSubRegionXCoordinate,destSubRegionYCoordinate,(int) layerInd))
							numberOfRoutingPerformed++;
						else
							notRoutedCount++;
//							notRoutedFlag=false;
							
						counter++;
						}
					
			}
			System.out.println("\nFOR TWO PIN NETS ROUTING PERFORMED: "+numberOfRoutingPerformed+" OUT OF "+counter+" percentage "+(((float)numberOfRoutingPerformed*100/(float)counter))+"%");
			System.out.println("ROUTING NOT PERFORMED FOR TWO PIN NETS: "+notRoutedCount+" OUT OF "+twoPinIntraLayerNetlistInformationList.size()+" PERCENTAGE: "+ ((float)notRoutedCount*100/(float)twoPinIntraLayerNetlistInformationList.size())+"%");
			
		}
	
	//THE THREE STEP APPROACH ALGORITHM FOR INTER/INTRA LAYER MULTI PIN AND INTER LAYER TWO PIN NETS ROUTING 
	public void performMultiPinInterLayerNetsRouting(){
		int numberOfRoutingPerformed=0;
		int counter =0;
		int notRoutedCountHere = 0;
		for(int i=0;i<multiplePinNetlistInformationList.size();i++){
			float backboneXCoordinate=(float)0.0;
			float backboneYCoordinate=(float)0.0;
			int backboneSubRegionXCoordinate = 0;
			int backboneSubRegionYCoordinate =0;
			float totalSenstivityOfAllTerminalsInNet = (float) 0.0;
			float sensitivityOfOneTerminal = (float) 0.0;
			int ct=0;
			boolean notRoutedFlag =true;
			
			incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity); // After each net routing,this amount of ineligibility factors will be added to those subregions, on which the path goes through
			//BACKBONE TREE CONSTRUCTION AND GETTING THE TERMINAL OF THE PSEUDO TERMINAL X AND Y COORDINATE
			//***********PROBLEM- HERE NOT FOR ALL NODES OF ONE NET THE SENSITIVITY INFORMATION IS NOT FOUND
//			Map<Integer,List<Integer>> subRegionToRoutePerLayerMap = new HashMap<Integer,List<Integer>>();
			List<List<Integer>> allSubRegionInforPerNetToRouteList = new ArrayList<List<Integer>>();
			List<Integer> oneSubRegionInforPerNetToRouteList;
			for(int j=0;j<multiplePinNetlistInformationList.get(i).size();j++){
				float terminalXCoordinate=(float)0.0;
				float terminalYCoordinate=(float)0.0;
				int subRegionXCoordinate = 0;
				int subRegionYCoordinate = 0;
				float layerInd = (float) 0.0;
//				float totalSenstivityOfAllTerminalsInNet = (float) 0.0;
//				List<Integer> subRegionInfoForMap = new ArrayList<Integer>();
				oneSubRegionInforPerNetToRouteList = new ArrayList<Integer>();
				terminalXCoordinate= Float.parseFloat(multiplePinNetlistInformationList.get(i).get(j).get(1))+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)).get(4);
//				placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0));
				terminalYCoordinate= Float.parseFloat(multiplePinNetlistInformationList.get(i).get(j).get(2))+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)).get(6);
//				System.out.println("terminal name:"+multiplePinNetlistInformationList.get(i).get(j).get(0)+"terminalXCoordinate..."+terminalXCoordinate+".......terminalYCoordinate..."+terminalYCoordinate);
				subRegionXCoordinate = (int)terminalXCoordinate/(int)subregionSizeInX;
				subRegionYCoordinate = (int)terminalYCoordinate/(int)subregionSizeInY;
				layerInd = placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)).get(7);
//				System.out.println("subRegionXCoordinate..."+subRegionXCoordinate+".......subRegionYCoordinate..."+subRegionYCoordinate+"layerInd...."+layerInd);
//				System.out.println("The node:"+multiplePinNetlistInformationList.get(i).get(j).get(0));
				oneSubRegionInforPerNetToRouteList.add(subRegionXCoordinate);
				oneSubRegionInforPerNetToRouteList.add(subRegionYCoordinate);
				oneSubRegionInforPerNetToRouteList.add((int) layerInd);
				
				allSubRegionInforPerNetToRouteList.add(oneSubRegionInforPerNetToRouteList);
				
				//CALCULATING THE X COORDINATE AND Y COORDINATE OF THE BACKBONE TREE FOR A MULTI TERMINAL NET
				for(int sInd=0;sInd<subregionInfoList.size();sInd++){
//					System.out.println(subRegionXCoordinate+" "+subregionInfoList.get(sInd).get(1)+":"+subRegionYCoordinate+" "+subregionInfoList.get(sInd).get(0)+":"+layerInd+" "+subregionInfoList.get(sInd).get(4));
					if((float)subRegionXCoordinate==subregionInfoList.get(sInd).get(1) && subRegionYCoordinate==subregionInfoList.get(sInd).get(0) && layerInd == subregionInfoList.get(sInd).get(4)){
						totalSenstivityOfAllTerminalsInNet+= subregionInfoList.get(sInd).get(7);
						sensitivityOfOneTerminal = subregionInfoList.get(sInd).get(7);
//						System.out.println("*********************************");
//						System.out.println("Matched "+multiplePinNetlistInformationList.get(i).get(j).get(0));
						ct++;
						break;
					}
				}
				
				backboneXCoordinate+=terminalXCoordinate *sensitivityOfOneTerminal;
				backboneYCoordinate+=terminalYCoordinate *sensitivityOfOneTerminal;
			}
			backboneXCoordinate=backboneXCoordinate/totalSenstivityOfAllTerminalsInNet;
			backboneYCoordinate=backboneYCoordinate/totalSenstivityOfAllTerminalsInNet;
			backboneSubRegionXCoordinate = (int) (backboneXCoordinate/(int)subregionSizeInX);
			backboneSubRegionYCoordinate = (int) (backboneYCoordinate/(int)subregionSizeInY);
//			System.out.println("THE TESTING"+ct+" "+multiplePinNetlistInformationList.get(i).size());
//			System.out.println("backboneXCoordinate..."+backboneXCoordinate+".......backboneYCoordinate..."+backboneYCoordinate+"\n");
//			System.out.println("backboneSubRegionXCoordinate..."+backboneSubRegionXCoordinate+".......backboneSubRegionYCoordinate..."+backboneSubRegionYCoordinate+"\n");
			
			
			for(List<Integer> oneTerminal: allSubRegionInforPerNetToRouteList){
//				System.out.println("STARTING TWO PIN NET ROUTING ..");
				if(backboneSubRegionXCoordinate<noOfSubregionInXDirection && backboneSubRegionYCoordinate<noOfSubregionInYDirection && oneTerminal.get(0)<noOfSubregionInXDirection && oneTerminal.get(1)<noOfSubregionInYDirection){
					if(twoTerminalRoutingPathTwoVar(backboneSubRegionXCoordinate,backboneSubRegionYCoordinate,oneTerminal.get(0),oneTerminal.get(1),oneTerminal.get(2)))
						numberOfRoutingPerformed++;
					else
						notRoutedFlag=false;
					counter++;
					}
				
//				System.out.println("ENDING TWO PIN NET ROUTING .."+counter);
			}
			if(!notRoutedFlag){
				notRoutedCount++;
				notRoutedCountHere++;
			}
		}
//		System.out.println("numberOfRoutingPerformed........"+numberOfRoutingPerformed+" for "+counter+" percentage "+(((float)numberOfRoutingPerformed*100/(float)counter))+"%");
		System.out.println("\nROUTING NOT PERFORMED FOR MULTI PIN NETS: "+notRoutedCountHere+" OUT OF "+multiplePinNetlistInformationList.size()+" PERCENTAGE:"+ ((float)notRoutedCountHere*100/(float)multiplePinNetlistInformationList.size())+"%");
		float percentageRoutable =((float)100.0-((float)notRoutedCount*100/(float)(multiplePinNetlistInformationList.size()+twoPinIntraLayerNetlistInformationList.size()))) ;
		System.out.println("\nACTUAL OVER ALL(INTER+INTRA) ROUTING PERFORMED : "+(multiplePinNetlistInformationList.size()+twoPinIntraLayerNetlistInformationList.size()-notRoutedCount)+" OUT OF "+(multiplePinNetlistInformationList.size()+twoPinIntraLayerNetlistInformationList.size())
				+" PERCENTAGE: "+ percentageRoutable +"%");
		
		System.out.println("The total Wirelength:"+totalWireLength);
		System.out.println("The average Wirelength per Net: "+(totalWireLength/(multiplePinNetlistInformationList.size()+twoPinIntraLayerNetlistInformationList.size())));
		System.out.println("The ratio of wirelength per miximum Manhattan Distance:"+((float)((totalWireLength/(multiplePinNetlistInformationList.size()+twoPinIntraLayerNetlistInformationList.size()))/(float)(noOfSubregionInXDirection+noOfSubregionInYDirection))));
		
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
			boundaryBoxYMaxCoordinate = (srcSubRegionYCoordinate<noOfSubregionInYDirection)?(srcSubRegionYCoordinate+2):(srcSubRegionYCoordinate+2);
		}else if(srcSubRegionXCoordinate==destSubRegionXCoordinate){//Allowing maximum 1 sub region detour in both the directions
			boundaryBoxXMinCoordinate = (srcSubRegionXCoordinate>0)?srcSubRegionXCoordinate-1:srcSubRegionXCoordinate;
			boundaryBoxYMinCoordinate = (srcSubRegionYCoordinate<destSubRegionYCoordinate)?srcSubRegionYCoordinate:destSubRegionYCoordinate;
			boundaryBoxXMaxCoordinate = (srcSubRegionXCoordinate<noOfSubregionInXDirection)?(srcSubRegionXCoordinate+2):(srcSubRegionXCoordinate+1);
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
//		System.out.println("THE IN ELIG FACTOR:"+subregionMap.get(strKey));
//		List<Float> ineligList;
		Float minVal = (float) 1.0;
		Float minValToAdd = minVal;
//		System.out.println("at starting...manhattanDistance.."+manhattanDistance);
//		System.out.println("TOTAL SUBREGION IN X AND Y DIRECTIONS:"+noOfSubregionInXDirection+" "+noOfSubregionInYDirection);
		
	
//		if(srcSubRegionXCoordinate<destSubRegionXCoordinate&&srcSubRegionYCoordinate>destSubRegionYCoordinate){
//			boundaryBoxXMinCoordinate=srcSubRegionXCoordinate;
//			boundaryBoxYMinCoordinate = destSubRegionYCoordinate;
//			boundaryBoxXMaxCoordinate = destSubRegionXCoordinate+1;
//			boundaryBoxYMaxCoordinate = srcSubRegionYCoordinate+1;
//		}
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
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
					}else//For Bottom and Upper neighbour
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
					favouredSubRegionXCoordinate = destSubRegionXCoordinate;
					favouredSubRegionYCoordinate = destSubRegionYCoordinate;
					manhattanDistance = 0;
					favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
					minValToAdd = subregionMap.get(favouredKeyStr);
//					minValToAdd = minVal;
					subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
			//		subregionMap.put(favouredKeyStr, minValToAdd);
//					System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
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
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
					}else if((destSubRegionYCoordinate-favouredSubRegionYCoordinate)==2){//For Upper neighbour
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
						favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
						favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
					}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-2){//For Left neighbour
						favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
						favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
					}else if((destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-2){//For Bottom neighbour
						incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
						favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
						favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
					}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==1){//For Right-up neighbour
					
						// Take the Right or Up subregion with minimum ineligibility factor
						if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
							float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
							ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//							if(ineligFactForRightNeigh>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//							System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
							if(ineligFactForRightNeigh<minVal){
								favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyRight);
								minVal= ineligFactForRightNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if444");
							}
						}
						if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
							float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
							ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//							if(neighUpSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
							if(ineligFactForUpNeigh<minVal){
								favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyUp);
								minVal= ineligFactForUpNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if2222");
							}
						}
						
						
					}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==1){//For Left-up neighbour
						
						// Take the Left or Up subregion with minimum ineligibility factor
						if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
							float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
							ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//							if(neighLeftSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
							if(ineligFactForLeftNeigh<minVal){
								favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyLeft);
								minVal= ineligFactForLeftNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if333");
							}
						}
						if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
							float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
							ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//							if(neighUpSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
							if(ineligFactForUpNeigh<minVal){
								favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyUp);
								minVal= ineligFactForUpNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if2222");
							}
						}
						
					}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==-1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-1){//For Left-bottom neighbour
						
						// Take the Left or Bottom subregion with minimum ineligibility factor
						if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
							float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
							ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//							if(neighLeftSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
							if(ineligFactForLeftNeigh<minVal){
								favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyLeft);
								minVal= ineligFactForLeftNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if333");
							}
						}
						if(neighBottomSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyBottom)!=null){
							float ineligFactForBottomNeigh=((neighBottomSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyBottom)+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyBottom));
							ineligFactForBottomNeigh = (float) ((ineligFactForBottomNeigh>=1.0)?1.0:ineligFactForBottomNeigh);
//							if(neighBottomSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
							if(ineligFactForBottomNeigh<minVal){
								favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyBottom);
								minVal = ineligFactForBottomNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if111");
							}
							
						}
						
					}else if((destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1 && (destSubRegionYCoordinate-favouredSubRegionYCoordinate)==-1){//For Right-bottom neighbour
						
						// Take the Right or Bottom subregion with minimum ineligibility factor
						if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
							float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
							ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//							if(ineligFactForRightNeigh>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//							System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
							if(ineligFactForRightNeigh<minVal){
								favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyRight);
								minVal= ineligFactForRightNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if444");
							}
						}
						if(neighBottomSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyBottom)!=null){
							float ineligFactForBottomNeigh=((neighBottomSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyBottom)+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyBottom));
							ineligFactForBottomNeigh = (float) ((ineligFactForBottomNeigh>=1.0)?1.0:ineligFactForBottomNeigh);
//							if(neighBottomSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
							if(ineligFactForBottomNeigh<minVal){
								favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyBottom);
								minVal = ineligFactForBottomNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if111");
							}
							
						}
						
					}
									
					manhattanDistance = 1;
					favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
					minValToAdd = subregionMap.get(favouredKeyStr);
//					minValToAdd = minVal;
					subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
				}
				else{ //FOR MANHATTAN DISTANCE GREATER THAN TWO
//						System.out.println("at starting.."+favouredSubRegionXCoordinate+" "+favouredSubRegionYCoordinate);
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
//							if(neighBottomSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
							if(ineligFactForBottomNeigh<minVal){
								favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyBottom);
								minVal = ineligFactForBottomNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if111");
							}
							
						}
						if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
							float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
							ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//							if(neighUpSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
							if(ineligFactForUpNeigh<minVal){
								favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyUp);
								minVal= ineligFactForUpNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/verticalCapacity);
//								System.out.println("in if2222");
							}
						}
						if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
							float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
							ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//							if(neighLeftSubRegionMD>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//							System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
							if(ineligFactForLeftNeigh<minVal){
								favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyLeft);
								minVal= ineligFactForLeftNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if333");
							}
						}
						if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
							float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
							ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//							if(ineligFactForRightNeigh>manhattanDistance)
//								System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//							System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
							if(ineligFactForRightNeigh<minVal){
								favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
								favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
								minValToAdd = subregionMap.get(strKeyRight);
								minVal= ineligFactForRightNeigh;
								incrementOfIneligibilityPerNetRouting = betaParamTwoVar * (1/horizontalCapacity);
//								System.out.println("in if444");
							}
						}
						manhattanDistance =  Math.abs(destSubRegionXCoordinate-favouredSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-favouredSubRegionYCoordinate);
						String favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
						subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
					//	subregionMap.put(favouredKeyStr, minValToAdd);
						tempSubregionMap.put(favouredKeyStr, (float) 1.0);
//						System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
						if(favouredSubRegionXCoordinate==noOfSubregionInXDirection||favouredSubRegionYCoordinate==noOfSubregionInYDirection)
							return false;
						
				}
//				System.out.println("at ending...manhattanDistance.."+manhattanDistance);
				countRoutingPathRepeated++;
			}
		}
//		System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
	//	System.out.println("The total number of Routing path: "+countRoutingPathRepeated);
		totalWireLength = totalWireLength+countRoutingPathRepeated;
		return true;
	}
	
	//Returns the routing path of the two pin connection and adjusts the ineligibility factors accordingly with 3 constraints
		private boolean twoTerminalRoutingPathThreeVar(int srcSubRegionXCoordinate,int srcSubRegionYCoordinate,int destSubRegionXCoordinate,int destSubRegionYCoordinate,int layerIndex){
			
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
				boundaryBoxYMaxCoordinate = (srcSubRegionYCoordinate<noOfSubregionInYDirection)?(srcSubRegionYCoordinate+2):(srcSubRegionYCoordinate+2);
			}else if(srcSubRegionXCoordinate==destSubRegionXCoordinate){//Allowing maximum 1 sub region detour in both the directions
				boundaryBoxXMinCoordinate = (srcSubRegionXCoordinate>0)?srcSubRegionXCoordinate-1:srcSubRegionXCoordinate;
				boundaryBoxYMinCoordinate = (srcSubRegionYCoordinate<destSubRegionYCoordinate)?srcSubRegionYCoordinate:destSubRegionYCoordinate;
				boundaryBoxXMaxCoordinate = (srcSubRegionXCoordinate<noOfSubregionInXDirection)?(srcSubRegionXCoordinate+2):(srcSubRegionXCoordinate+1);
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
//			System.out.println("THE IN ELIG FACTOR:"+subregionMap.get(strKey));
//			List<Float> ineligList;
			Float minVal = (float) 1.0;
			Float minValToAdd = minVal;
//			System.out.println("at starting...manhattanDistance.."+manhattanDistance);
//			System.out.println("TOTAL SUBREGION IN X AND Y DIRECTIONS:"+noOfSubregionInXDirection+" "+noOfSubregionInYDirection);
			
		
//			if(srcSubRegionXCoordinate<destSubRegionXCoordinate&&srcSubRegionYCoordinate>destSubRegionYCoordinate){
//				boundaryBoxXMinCoordinate=srcSubRegionXCoordinate;
//				boundaryBoxYMinCoordinate = destSubRegionYCoordinate;
//				boundaryBoxXMaxCoordinate = destSubRegionXCoordinate+1;
//				boundaryBoxYMaxCoordinate = srcSubRegionYCoordinate+1;
//			}
//				
			int countRoutingPathRepeated = 0;
			
			while(manhattanDistance>0){
				if(countRoutingPathRepeated>2*manhattanDistanceMax)
					return false;
				else{	
					if(manhattanDistance==1){
						String favouredKeyStr;
						if(Math.abs(destSubRegionXCoordinate-favouredSubRegionXCoordinate)==1){//For Left or right neighbour
							incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/horizontalCapacity);
						}else//For Bottom and Upper neighbour
							incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/verticalCapacity);
						favouredSubRegionXCoordinate = destSubRegionXCoordinate;
						favouredSubRegionYCoordinate = destSubRegionYCoordinate;
						manhattanDistance = 0;
						favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
						minValToAdd = subregionMap.get(favouredKeyStr);
//						minValToAdd = minVal;
						subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
//						System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
					}
					else{
//							System.out.println("at starting.."+favouredSubRegionXCoordinate+" "+favouredSubRegionYCoordinate);
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
//								if(neighBottomSubRegionMD>manhattanDistance)
//									System.out.println("TEST IF:"+tempSubregionMap.get(strKeyBottom)+" "+((float)neighBottomSubRegionMD/(float)manhattanDistanceMax));
//								System.out.println("TEST1:"+neighBottomSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyBottom)+" "+ineligFactForBottomNeigh+" "+minVal);
								if(ineligFactForBottomNeigh<minVal){
									favouredSubRegionXCoordinate = neighBottomSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighBottomSubRegionYCoordinate;
									minValToAdd = subregionMap.get(strKeyBottom);
									minVal = ineligFactForBottomNeigh;
									incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/verticalCapacity);
//									System.out.println("in if111");
								}
								
							}
							if(neighUpSubRegionYCoordinate!=-1&&tempSubregionMap.get(strKeyUp)!=null){
								float ineligFactForUpNeigh=((neighUpSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyUp)+((float)neighUpSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyUp));
								ineligFactForUpNeigh = (float) ((ineligFactForUpNeigh>=1.0)?1.0:ineligFactForUpNeigh);
//								if(neighUpSubRegionMD>manhattanDistance)
//									System.out.println("TEST IF:"+tempSubregionMap.get(strKeyUp)+" "+((float)neighUpSubRegionMD/(float)manhattanDistanceMax));
//								System.out.println("TEST2:"+neighUpSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyUp)+" "+ineligFactForUpNeigh+" "+minVal);
								if(ineligFactForUpNeigh<minVal){
									favouredSubRegionXCoordinate = neighUpSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighUpSubRegionYCoordinate;
									minValToAdd = subregionMap.get(strKeyUp);
									minVal= ineligFactForUpNeigh;
									incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/verticalCapacity);
//									System.out.println("in if2222");
								}
							}
							if(neighLeftSubRegionXCoordinate!=-1&&tempSubregionMap.get(strKeyLeft)!=null){
								float ineligFactForLeftNeigh=((neighLeftSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyLeft)+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyLeft));
								ineligFactForLeftNeigh = (float) ((ineligFactForLeftNeigh>=1.0)?1.0:ineligFactForLeftNeigh);
//								if(neighLeftSubRegionMD>manhattanDistance)
//									System.out.println("TEST IF:"+tempSubregionMap.get(strKeyLeft)+" "+((float)neighLeftSubRegionMD/(float)manhattanDistanceMax));
//								System.out.println("TEST3:"+neighLeftSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyLeft)+" "+ineligFactForLeftNeigh+" "+minVal);
								if(ineligFactForLeftNeigh<minVal){
									favouredSubRegionXCoordinate = neighLeftSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighLeftSubRegionYCoordinate;
									minValToAdd = subregionMap.get(strKeyLeft);
									minVal= ineligFactForLeftNeigh;
									incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/horizontalCapacity);
//									System.out.println("in if333");
								}
							}
							if(neighRightSubRegionXCoordinate!=-1 && tempSubregionMap.get(strKeyRight)!=null){
								float ineligFactForRightNeigh=((neighRightSubRegionMD>manhattanDistance)?(tempSubregionMap.get(strKeyRight)+((float)neighRightSubRegionMD/(float)manhattanDistanceMax)):tempSubregionMap.get(strKeyRight));
								ineligFactForRightNeigh = (float) ((ineligFactForRightNeigh>=1.0)?1.0:ineligFactForRightNeigh);
//								if(ineligFactForRightNeigh>manhattanDistance)
//									System.out.println("TEST IF:"+tempSubregionMap.get(strKeyRight)+" "+((float)ineligFactForRightNeigh/(float)manhattanDistanceMax));
//								System.out.println("TEST4:"+neighRightSubRegionMD+" "+manhattanDistance+" "+tempSubregionMap.get(strKeyRight)+" "+ineligFactForRightNeigh+" "+minVal);
								if(ineligFactForRightNeigh<minVal){
									favouredSubRegionXCoordinate = neighRightSubRegionXCoordinate;
									favouredSubRegionYCoordinate = neighRightSubRegionYCoordinate;
									minValToAdd = subregionMap.get(strKeyRight);
									minVal= ineligFactForRightNeigh;
									incrementOfIneligibilityPerNetRouting = betaParamThreeVar * (1/horizontalCapacity);
//									System.out.println("in if444");
								}
							}
							manhattanDistance =  Math.abs(destSubRegionXCoordinate-favouredSubRegionXCoordinate) + Math.abs(destSubRegionYCoordinate-favouredSubRegionYCoordinate);
							String favouredKeyStr= "x"+String.valueOf(favouredSubRegionXCoordinate)+"y"+String.valueOf(favouredSubRegionYCoordinate);
							subregionMap.put(favouredKeyStr, (minValToAdd+incrementOfIneligibilityPerNetRouting));
							tempSubregionMap.put(favouredKeyStr, (float) 1.0);
//							System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
							if(favouredSubRegionXCoordinate==noOfSubregionInXDirection||favouredSubRegionYCoordinate==noOfSubregionInYDirection)
								return false;
							
					}
//					System.out.println("at ending...manhattanDistance.."+manhattanDistance);
					countRoutingPathRepeated++;
				}
			}
//			System.out.println("favouredSubRegionXCoordinate...."+favouredSubRegionXCoordinate+"favouredSubRegionYCoordinate..."+favouredSubRegionYCoordinate+"layerIndex...."+layerIndex);
			return true;
		}
	
	public void recogniseMultiPinIntraLayerNets(){
		
	}
	public void recogniseTwoPinIntraLayerNets(){
		
	}
	//PRINTING THE RESULTANT ROUTE FOR ALL THE NETS IN A FILE
	public void printRoutesForNetListToFile(){
		
	}
	
	public void writeToFile(){
		try {
			String outFileName = "";
			outFileName = getInpNETFileName().substring(
					getInpNETFileName().lastIndexOf("\\") + 1);
			FileWriter fstream = new FileWriter(getInpNETFileName().substring(0,
					getInpNETFileName().lastIndexOf("."))
					+ ".out");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(getTotalNumberOfNet().longValue()+" "+getTotalNumberOfPins().longValue()+" "+getNoOfLayers().intValue()+"\n");
			Iterator<String> iterator = placementInformationMap.keySet().iterator();  
			
			out.write("NetDegree:"+netDegreeList.size()+"\n");
//			for(Integer in:netDegreeList){
//				out.write(in+" ");
//			}
			out.write("\n"+ placementInformationMap.size()+"\n");  
			while (iterator.hasNext()) {  
			   String key = iterator.next().toString();  
			   List<Float> fl = placementInformationMap.get(key); 
			   out.write(key+"\t\t");
			   for(Float ft: fl ){
					out.write(ft+"\t\t");
				}
				out.write("\n");  
			} 
			Iterator<String> iterator1 = sensitivityRatioInformationMap.keySet().iterator(); 
			out.write("\nThe sensitivity information"+ sensitivityRatioInformationMap.size()+"\n");  
			while (iterator1.hasNext()) {  
			   String key = iterator1.next().toString();  
			   Float fl = sensitivityRatioInformationMap.get(key); 
			   out.write(key+"\t\t"+fl);
			   out.write("\n");  
			} 
			out.write("\n"); 
//			out.write("after placement\n\n"+getNetlistInformationList().size());  
//			for(List<List<String>> tt: getNetlistInformationList()){
//				for(List<String> lt: tt){
//					for(String it:lt ){
//						out.write(it+" ");
//					}
//					out.write("\n");
//				}
//				out.write("\n\n");
//			}
			
			out.write("ONLY MULTIPLE PIN NETS ARE:\n\n"+multiplePinNetlistInformationList.size()+"\n");  
			for(int i=0;i<multiplePinNetlistInformationList.size();i++){
				for(int j=0;j<multiplePinNetlistInformationList.get(i).size();j++){
//					out.write(lt.get(0)+"\n");
//					System.out.println(multiplePinNetlistInformationList.get(i).get(j).get(0)+" "+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)).get(7));
					out.write(multiplePinNetlistInformationList.get(i).get(j).get(0)+" "+placementInformationMap.get(multiplePinNetlistInformationList.get(i).get(j).get(0)).get(7));
//					for(String it:lt ){
////						out.write(it+" ");
//					}
					out.write("\n");
				}
				out.write("\n\n");
			}
//			out.write("THE SUBREGION INFORMATION LIST:\n");
//			for(List<Float> lt: subregionInfoList){
//				for(Float ft:lt)
//					out.write(ft+"\t\t");
//				out.write("\n");
//			}
			// Close the output stream
			out.close();
			fstream.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
