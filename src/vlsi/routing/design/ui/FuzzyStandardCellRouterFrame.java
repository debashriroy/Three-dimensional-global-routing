package vlsi.routing.design.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import vlsi.routing.design.process.MixedRouterProcess;
import vlsi.routing.design.process.StandardCellRouterProcess;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dell
 */
public class FuzzyStandardCellRouterFrame extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Creates new form MixedRouterFrame
     */
    public FuzzyStandardCellRouterFrame() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("StandardCellRouter");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
 // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        famePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        netFilenameTextfield = new javax.swing.JTextField();
        netBrowseButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        plFileNameTextField = new javax.swing.JTextField();
        plBrowseButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        infoFileTextField = new javax.swing.JTextField();
        infoBrowseButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        grFileTextField = new javax.swing.JTextField();
        grBrowseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Enter the .net file: ");

        netFilenameTextfield.setText("");

        netBrowseButton.setText("Browse...");
        netBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netBrowseButtonActionPerformed(evt);
            }
        });

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Enter the Placement File:");

        plFileNameTextField.setText("");
//        plFileNameTextField.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                plFileNameTextFieldActionPerformed(evt);
//            }
//        });

        plBrowseButton.setText("Browse...");
        plBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plBrowseButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Enter the sensitivity info file name:");

        infoFileTextField.setText("");

        infoBrowseButton.setText("Browse...");
        infoBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoBrowseButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Enter the .gr file: ");

        grFileTextField.setText("");
//        grFileTextField.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                grFileTextFieldActionPerformed(evt);
//            }
//        });

        grBrowseButton.setText("Browse...");
        grBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout famePanelLayout = new javax.swing.GroupLayout(famePanel);
        famePanel.setLayout(famePanelLayout);
        famePanelLayout.setHorizontalGroup(
            famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(famePanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(famePanelLayout.createSequentialGroup()
                        .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(135, 135, 135))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, famePanelLayout.createSequentialGroup()
                        .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(grFileTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netFilenameTextfield, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(plFileNameTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoFileTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(netBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(plBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(infoBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(grBrowseButton))
                        .addGap(35, 35, 35))))
        );
        famePanelLayout.setVerticalGroup(
            famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(famePanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netFilenameTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netBrowseButton))
                .addGap(25, 25, 25)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(plFileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(plBrowseButton))
                .addGap(35, 35, 35)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(infoFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(grFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grBrowseButton))
                .addGap(46, 46, 46)
                .addGroup(famePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateButton)
                    .addComponent(closeButton))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(famePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(famePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>             

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
       FuzzyStandardCellRouterFrame.this.dispose();
    }                                           

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
       StandardCellRouterProcess standardRouter = new StandardCellRouterProcess();
       standardRouter.setInpNETFileName(netFilenameTextfield.getText());
       standardRouter.setInpPLFileName(plFileNameTextField.getText());
       standardRouter.setInpINFOFileName(infoFileTextField.getText());
       
       standardRouter.setInpGRFileName(grFileTextField.getText());
       
       MixedRouterProcess mixedRouter = new MixedRouterProcess();
       mixedRouter.setInpGRFileName(grFileTextField.getText());
       
       if(grFileTextField.getText().equals(""))
       		standardRouter.implementRouter();
       else 
    	   mixedRouter.implementRouter();
    }                                              

     private void netBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                    
    	JFileChooser inpPlaFileChooser;
		if (netFilenameTextfield.getText().equals(""))
			inpPlaFileChooser = new JFileChooser();
		else
			inpPlaFileChooser = new JFileChooser(netFilenameTextfield.getText());

		inpPlaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		inpPlaFileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".nets")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.nets";
			}
		});
		int returnVal = inpPlaFileChooser.showOpenDialog(FuzzyStandardCellRouterFrame.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// File file = inpPlaFile.getSelectedFile();
			netFilenameTextfield.setText(inpPlaFileChooser.getSelectedFile()
					.getAbsolutePath());
			//errorLabel.setText("");
		}
    }                                                   

    private void plBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
    	JFileChooser inpPlaFileChooser;
    	
		if (netFilenameTextfield.getText().equals(""))
			inpPlaFileChooser = new JFileChooser();
		else
			inpPlaFileChooser = new JFileChooser(netFilenameTextfield.getText());

		inpPlaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		inpPlaFileChooser.setFileFilter(new FileFilter() {                       
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".pl")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.pl";
			}
		});
		
		int returnVal = inpPlaFileChooser.showOpenDialog(FuzzyStandardCellRouterFrame.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// File file = inpPlaFile.getSelectedFile();
			plFileNameTextField.setText(inpPlaFileChooser.getSelectedFile()
					.getAbsolutePath());
			System.out.println(plFileNameTextField.getText());
			//errorLabel.setText("");
		}
    }   
    
    private void infoBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
    	JFileChooser inpPlaFileChooser;
		if (netFilenameTextfield.getText().equals(""))
			inpPlaFileChooser = new JFileChooser();
		else
			inpPlaFileChooser = new JFileChooser(netFilenameTextfield.getText());

		inpPlaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		inpPlaFileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".info")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.info";
			}
		});
		int returnVal = inpPlaFileChooser.showOpenDialog(FuzzyStandardCellRouterFrame.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// File file = inpPlaFile.getSelectedFile();
			infoFileTextField.setText(inpPlaFileChooser.getSelectedFile()
					.getAbsolutePath());
			//errorLabel.setText("");
		}
    }  
    
    private void grBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
    	JFileChooser inpPlaFileChooser;
    	
//    	System.out.println("Printingg..."+netFilenameTextfield.getText());
		if (grFileTextField.getText().equals(""))
			inpPlaFileChooser = new JFileChooser();
		else
			inpPlaFileChooser = new JFileChooser(grFileTextField.getText());
//    	inpPlaFileChooser = new JFileChooser();

		inpPlaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		inpPlaFileChooser.setFileFilter(new FileFilter() {                       
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".gr")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "*.gr";
			}
		});
		
		int returnVal = inpPlaFileChooser.showOpenDialog(FuzzyStandardCellRouterFrame.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// File file = inpPlaFile.getSelectedFile();
			grFileTextField.setText(inpPlaFileChooser.getSelectedFile()
					.getAbsolutePath());
			//errorLabel.setText("");
		}
    }                                              

//    private void grFileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
//        // TODO add your handling code here:
//    	
//    }                 


    /**
     * @param args the command line arguments
     */
 // Variables declaration - do not modify                     
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel famePanel;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton grBrowseButton;
   	private javax.swing.JTextField grFileTextField;
    private javax.swing.JButton infoBrowseButton;
    private javax.swing.JTextField infoFileTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton netBrowseButton;
    private javax.swing.JTextField netFilenameTextfield;
    private javax.swing.JButton plBrowseButton;
    private javax.swing.JTextField plFileNameTextField;        
}
