package vlsi.routing.design;

import javax.swing.UIManager;


import vlsi.routing.design.ui.FuzzyStandardCellRouterFrame;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		 try {
	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	                if ("Nimbus".equals(info.getName())) {
	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	            }
	        } catch (ClassNotFoundException ex) {
	            java.util.logging.Logger.getLogger(FuzzyStandardCellRouterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (InstantiationException ex) {
	            java.util.logging.Logger.getLogger(FuzzyStandardCellRouterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (IllegalAccessException ex) {
	            java.util.logging.Logger.getLogger(FuzzyStandardCellRouterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
	            java.util.logging.Logger.getLogger(FuzzyStandardCellRouterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        }
	        //</editor-fold>

	        /* Create and display the form */
	        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.put("swing.boldMetal", Boolean.FALSE); 
			//		MixedRouterFrame frame = new MixedRouterFrame();
					FuzzyStandardCellRouterFrame frame = new FuzzyStandardCellRouterFrame();
					//frame.pack();
					frame.setVisible(true);
//					MPRMGeneration genMPRM= new MPRMGeneration();
//					genMPRM.implementTool();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}