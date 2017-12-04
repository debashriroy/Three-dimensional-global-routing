package vlsi.routing.design.process;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class MixedRouterProcess {
	
	public String inpGRFileName;
	//public String opGRFileName;

	public String getInpGRFileName() {
		return inpGRFileName;
	}

	public void setInpGRFileName(String inpGRFileName) {
		this.inpGRFileName = inpGRFileName;
	}
	
	public void implementRouter(){
		
		
		MixedRouterProcessImplementation routerImplementation = new MixedRouterProcessImplementation();
		//READING THE GLOBAL ROUTING BENCH MARK FILES AND SETTING THE PARAMETERS
		routerImplementation.setInpGRFileName(inpGRFileName);
		
		
		routerImplementation.readGRFileAndPopulateParams();
		//WRITING THE GLOBAL ROUTING PARAMETERS 
//		routerImplementation.writeToFile();
		
		//GENERATE GUIDING INFORMATION
		long milisStart= new Date().getTime();
		routerImplementation.generatePreRoutingGuidingInformation();
		long milisGuideInfo= new Date().getTime();
		float timeRequiredGuideInfo= (milisGuideInfo-milisStart)/1000;
//		System.out.println("\nTHE REQUIRED TIME NEEDED FOR GUIDING INFORMATION GENERATION:"+(int)(timeRequiredGuideInfo/3600)+" hr "+(int)((timeRequiredGuideInfo%3600)/60)+ " m "+(timeRequiredGuideInfo%3600)%60+ " s ");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR GUIDING INFORMATION GENERATION:"+(timeRequiredGuideInfo/60)+ " m ");
		
		//RECOGNISE TWO PIN AND MULTIPIN NETS
		routerImplementation.recogniseMultiANDTwoPinInterLayerNets();
		//PERFORM TWO PIN NET ROUTING
		routerImplementation.performTwoPinNetsRouting();
		long milisTwoIntra= new Date().getTime();
		float timeRequired2PinInta= (milisTwoIntra-milisGuideInfo)/1000;
//		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING 2 PIN INTRA LAYER NETS ROUTING OPERATION:"+(int)(timeRequired2PinInta/3600)+" hr "+(int)((timeRequired2PinInta%3600)/60)+ " m "+(timeRequired2PinInta%3600)%60+ " s ");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING 2 PIN INTRA LAYER NETS ROUTING OPERATION:"+(timeRequired2PinInta/60)+ " m ");
		
		//PERFORM MULTI PIN NET ROUTING
		routerImplementation.performMultiPinNetsRouting();
		long milisMultiNet= new Date().getTime();
		
		
		
		float timeRequiredMultiNet= (milisMultiNet-milisGuideInfo)/1000;
		
//		System.out.println("\nTHE REQUIRED TIME NEEDED FOR GUIDING INFORMATION GENERATION:"+(int)(timeRequiredGuideInfo/3600)+" hr "+(int)((timeRequiredGuideInfo%3600)/60)+ " m "+(timeRequiredGuideInfo%3600)%60+ " s ");
//		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING 2 PIN INTRA LAYER NETS ROUTING OPERATION:"+(int)(timeRequired2PinInta/3600)+" hr "+(int)((timeRequired2PinInta%3600)/60)+ " m "+(timeRequired2PinInta%3600)%60+ " s ");
//		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING ALL ROUTING OPERATION:"+(int)(timeRequiredMultiNet/3600)+" hr "+(int)((timeRequiredMultiNet%3600)/60)+ " m "+(timeRequiredMultiNet%3600)%60+ " s ");
		
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR GUIDING INFORMATION GENERATION:"+(timeRequiredGuideInfo/60)+ " m ");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING 2 PIN INTRA LAYER NETS ROUTING OPERATION:"+(timeRequired2PinInta/60)+ " m ");
		System.out.println("\nTHE REQUIRED TIME NEEDED FOR PERFORMING ALL ROUTING OPERATION:"+(timeRequiredMultiNet/60)+ " m ");
		System.out.println("Output for file: "+ getInpGRFileName());
	}

}
