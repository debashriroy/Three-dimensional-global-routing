package vlsi.routing.design.process;

import java.util.Date;
public class StandardCellRouterProcess {
	
	public String inpNETFileName;
	public String inpPLFileName;
	public String inpINFOFileName;
	
	public String inpGRFileName;//For ISPD 2007 and 2008 Benhmark Circuits
	//public String opGRFileName;
	public float gridSize=(float) 1.0;//MAY BE NEEDED TO TAKE FROM USER
	public float subregionSize=(float) 10.0;// the size will be the nth multiple of gridsize

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

	public float getGridSize() {
		return gridSize;
	}

	public void setGridSize(float gridSize) {
		this.gridSize = gridSize;
	}

	public float getSubregionSize() {
		return subregionSize;
	}

	public void setSubregionSize(float subregionSize) {
		this.subregionSize = subregionSize;
	}

	public String getInpGRFileName() {
		return inpGRFileName;
	}

	public void setInpGRFileName(String inpGRFileName) {
		this.inpGRFileName = inpGRFileName;
	}

	public void implementRouter(){
		StandardCellRouterProcessImplementation routerImplementation = new StandardCellRouterProcessImplementation();
		//READING THE GLOBAL ROUTING BENCH MARK(ISPD 1998) FILES AND SETTING THE PARAMETERS
		routerImplementation.setInpNETFileName(inpNETFileName);
		routerImplementation.setInpPLFileName(inpPLFileName);
		routerImplementation.setInpINFOFileName(inpINFOFileName);

		routerImplementation.readPLFileAndPopulateParams();
		routerImplementation.readNETSFileAndPopulateParams();
		routerImplementation.readINFOFileAndPopulateParams();
//		routerImplementation.findMaxWidthOfLayout();
		routerImplementation.findNotPlacementedNode();
		System.out.println("COMPLETE READING INPUT FILES");
		
		long milisStart= new Date().getTime();
		routerImplementation.generatePreRoutingGuidingInformationForTwoVar();
		long milisGuideInfo= new Date().getTime();
		
		routerImplementation.recogniseMultiANDTwoPinInterLayerNets();
		routerImplementation.performTwoPinIntraLayerNetsRouting();
		long milisTwoIntra= new Date().getTime();
		
		routerImplementation.performMultiPinInterLayerNetsRouting();
		routerImplementation.printRoutesForNetListToFile();
		long milisMultiNet= new Date().getTime();
		
		float timeRequiredGuideInfo= (milisGuideInfo-milisStart)/1000;
		float timeRequired2PinInta= (milisTwoIntra-milisGuideInfo)/1000;
		float timeRequiredMultiNet= (milisMultiNet-milisGuideInfo)/1000;
		
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR GUIDING INFORMATION GENERATION:"+timeRequiredGuideInfo+" seconds");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING 2 PIN INTRA LAYER NETS ROUTING OPERATION:"+timeRequired2PinInta+" seconds");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING ALL ROUTING OPERATION:"+timeRequiredMultiNet+" seconds");
		
				
		//GETTING THE REQUIRED PARAMETERS AND WRITING IT INTO A FILE FOR FUTURE USE
//		routerImplementation.writeToFile();
		//IMPLEMENTING THE MULTI-TERMINAL INTER-LATER ROUTING ALGORITHM
	}
	
//	public void implementGRRouter(){
//		StandardCellRouterProcessImplementation routerImplementation = new StandardCellRouterProcessImplementation();
//		routerImplementation.setInpGRFileName(inpGRFileName);
//
//		routerImplementation.readGRFileAndPopulateParams();
//		System.out.println("COMPLETE READING INPUT(BENCHMARK ISPD 2007 AND 2008) FILES");
//
//	}
}
