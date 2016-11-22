package crypton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class CryptonApp {
	
	JFrame frame;
	private JMenu mnAlgorithmusEinstellen;
	private static JMenu mnSchlssellngeEinstellen;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private static JRadioButton rdbtnNewRadioButton_2;
	static JRadioButtonMenuItem rdbtnmntmBit;
	static JRadioButtonMenuItem rdbtnmntmBit_1;
	static JRadioButtonMenuItem rdbtnmntmBit_2;
	static JRadioButtonMenuItem rdbtnmntmBit_3;
	static JRadioButtonMenuItem rdbtnmntmBit_4;
	static JRadioButtonMenuItem rdbtnmntmBit_5;
	private static JRadioButtonMenuItem rdbtnmntmAessymmetrisch;
	private static JRadioButtonMenuItem rdbtnmntmRsaasymmetrisch;
	private static JButton btnNewButton;
	private static JButton btnNewButton_1;
	private static JButton btnNewButton_2;
	private static JTextField textField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static ButtonGroup group_options;
	private static ButtonGroup group_algo;
	static ButtonGroup group_key;
	static File keyDir;
	static SecretKey pass_key;
	volatile static File private_key;
	volatile static File public_key;
	volatile static File file;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CryptonApp window = new CryptonApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CryptonApp() throws IOException {
		initialize();
		loadParameter();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 415, 415);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Crypton for FFHS // copybright by sewy&adpe");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame, "Wollen Sie die Einstellungen speichern?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					saveParameter();
					if(JOptionPane.showConfirmDialog(frame, "Wollen Sie das Programm beenden?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					if(JOptionPane.showConfirmDialog(frame, "Wollen Sie das Programm beenden?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
		    }
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmDateiVerschluesseln = new JMenuItem("Datei verschl\u00FCsseln");
		mntmDateiVerschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				encrypt_data();
			}
		});
		mnDatei.add(mntmDateiVerschluesseln);
		
		JMenuItem mntmDateiEntschluesseln = new JMenuItem("Datei entschl\u00FCsseln");
		mntmDateiEntschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decrypt_data();
			}
		});
		mnDatei.add(mntmDateiEntschluesseln);
		
		JMenuItem mntmSchluesselGenerieren  = new JMenuItem("Schl\u00FCssel generieren");
		mntmSchluesselGenerieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate_keys();
			}
		});
		mnDatei.add(mntmSchluesselGenerieren);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(frame, "Wollen Sie die Einstellungen speichern?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					saveParameter();
				}
			}
		});
		mnDatei.add(mntmSpeichern);
		
		JMenuItem mntmZurcksetzen = new JMenuItem("Zur\u00FCcksetzen");
		mntmZurcksetzen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAll();
			}
		});
		mnDatei.add(mntmZurcksetzen);
		
		JSeparator separator_1 = new JSeparator();
		mnDatei.add(separator_1);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(frame, "Wollen Sie die Einstellungen speichern?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					saveParameter();
					if(JOptionPane.showConfirmDialog(frame, "Wollen Sie das Programm beenden?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					if(JOptionPane.showConfirmDialog(frame, "Wollen Sie das Programm beenden?", "Hinweis!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			}
		});
		mnDatei.add(mntmBeenden);
		
		JMenu mnBearbeiten = new JMenu("Einstellungen");
		menuBar.add(mnBearbeiten);
		
		mnAlgorithmusEinstellen = new JMenu("Algorithmus einstellen");
		mnBearbeiten.add(mnAlgorithmusEinstellen);
		
		group_algo = new ButtonGroup();
		rdbtnmntmAessymmetrisch = new JRadioButtonMenuItem("AES (symmetrisch)");
		rdbtnmntmAessymmetrisch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnmntmBit_3.isSelected() || rdbtnmntmBit_4.isSelected() ||  
					rdbtnmntmBit_5.isSelected() == true ) { group_key.clearSelection();}	
				
				// Felder aktualisieren
				rdbtnmntmBit.setEnabled(true);
				rdbtnmntmBit_1.setEnabled(true);
				rdbtnmntmBit_2.setEnabled(true);
				rdbtnmntmBit_3.setEnabled(false);
				rdbtnmntmBit_4.setEnabled(false);
				rdbtnmntmBit_5.setEnabled(false);
				
				// Dateien aktualisieren
				if (rdbtnNewRadioButton.isSelected() || rdbtnNewRadioButton_1.isSelected() == true) { 
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
					btnNewButton_2.setEnabled(true);
					textField_1.setText(null);
				}
			}
		});
		
		mnAlgorithmusEinstellen.add(rdbtnmntmAessymmetrisch);
		
		rdbtnmntmRsaasymmetrisch = new JRadioButtonMenuItem("RSA (asymmetrisch)");
		rdbtnmntmRsaasymmetrisch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnmntmBit.isSelected() || rdbtnmntmBit_1.isSelected() ||  
						rdbtnmntmBit_2.isSelected() == true ) { group_key.clearSelection();}
				
				// Felder aktualisieren
				rdbtnmntmBit.setEnabled(false);
				rdbtnmntmBit_1.setEnabled(false);
				rdbtnmntmBit_2.setEnabled(false);
				rdbtnmntmBit_3.setEnabled(true);
				rdbtnmntmBit_4.setEnabled(true);
				rdbtnmntmBit_5.setEnabled(true);
				
				// Dateien und Textfelder aktualisieren
				if (rdbtnNewRadioButton.isSelected() == true) { 
					btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(true);
					btnNewButton_2.setEnabled(true);
					textField.setText(null);
				} else if (rdbtnNewRadioButton_1.isSelected() == true) {
					btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
					btnNewButton_2.setEnabled(true);
					textField_1.setText(null);
				}
			}
		});
		group_algo.add(rdbtnmntmAessymmetrisch);
		group_algo.add(rdbtnmntmRsaasymmetrisch);
		mnAlgorithmusEinstellen.add(rdbtnmntmRsaasymmetrisch);
		
		mnSchlssellngeEinstellen = new JMenu("Schl\u00FCssell\u00E4nge einstellen");
		mnBearbeiten.add(mnSchlssellngeEinstellen);
		
		rdbtnmntmBit = new JRadioButtonMenuItem("128 bit");
		rdbtnmntmBit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmAessymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit_3.setEnabled(false);
				rdbtnmntmBit_4.setEnabled(false);
				rdbtnmntmBit_5.setEnabled(false);
			}
		});
		group_key = new ButtonGroup();
		group_key.add(rdbtnmntmBit);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit);
		
		rdbtnmntmBit_1 = new JRadioButtonMenuItem("192 bit");
		rdbtnmntmBit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmAessymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit_3.setEnabled(false);
				rdbtnmntmBit_4.setEnabled(false);
				rdbtnmntmBit_5.setEnabled(false);
			}
		});
		group_key.add(rdbtnmntmBit_1);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit_1);
		
		rdbtnmntmBit_2 = new JRadioButtonMenuItem("256 bit");
		rdbtnmntmBit_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmAessymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit_3.setEnabled(false);
				rdbtnmntmBit_4.setEnabled(false);
				rdbtnmntmBit_5.setEnabled(false);
			}
		});
		group_key.add(rdbtnmntmBit_2);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit_2);
		
		rdbtnmntmBit_3 = new JRadioButtonMenuItem("1024 bit");
		rdbtnmntmBit_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmRsaasymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit.setEnabled(false);
				rdbtnmntmBit_1.setEnabled(false);
				rdbtnmntmBit_2.setEnabled(false);
			}
		});
		group_key.add(rdbtnmntmBit_3);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit_3);
		
		rdbtnmntmBit_4 = new JRadioButtonMenuItem("2048 bit");
		rdbtnmntmBit_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmRsaasymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit.setEnabled(false);
				rdbtnmntmBit_1.setEnabled(false);
				rdbtnmntmBit_2.setEnabled(false);
			}
		});
		group_key.add(rdbtnmntmBit_4);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit_4);
		
		rdbtnmntmBit_5 = new JRadioButtonMenuItem("4096 bit");
		rdbtnmntmBit_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnmntmRsaasymmetrisch.setSelected(true);
				
				// Felder aktualisieren
				rdbtnmntmBit.setEnabled(false);
				rdbtnmntmBit_1.setEnabled(false);
				rdbtnmntmBit_2.setEnabled(false);
			}
		});
		group_key.add(rdbtnmntmBit_5);
		mnSchlssellngeEinstellen.add(rdbtnmntmBit_5);
		
		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);
		
		JMenuItem mntmAnleitung = new JMenuItem("Anleitung");
		mnHilfe.add(mntmAnleitung);
		
		JMenuItem mntmberCrypton = new JMenuItem("\u00DCber Crypton");
		mnHilfe.add(mntmberCrypton);
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(panel1, BorderLayout.NORTH);
		GridBagLayout gbl_panel1 = new GridBagLayout();
		gbl_panel1.columnWidths = new int[]{0, 0};
		gbl_panel1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel1.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		panel1.setLayout(gbl_panel1);
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(new TitledBorder(new LineBorder(new Color(191, 205, 219)), "Optionen", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.insets = new Insets(0, 0, 5, 0);
		gbc_panel2.anchor = GridBagConstraints.WEST;
		gbc_panel2.fill = GridBagConstraints.VERTICAL;
		gbc_panel2.gridx = 0;
		gbc_panel2.gridy = 0;
		panel1.add(panel2, gbc_panel2);
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{0, 0};
		gbl_panel2.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		rdbtnNewRadioButton = new JRadioButton("Datei verschl\u00FCsseln");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				encrypt_data();
			}
		});
		group_options = new ButtonGroup();
		group_options.add(rdbtnNewRadioButton);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 0;
		panel2.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Datei entschl\u00FCsseln");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decrypt_data();
			}
		});
		group_options.add(rdbtnNewRadioButton_1);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 1;
		panel2.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Schl\u00FCssel generieren");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generate_keys();
			}
		});
		group_options.add(rdbtnNewRadioButton_2);
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_2.gridx = 0;
		gbc_rdbtnNewRadioButton_2.gridy = 2;
		panel2.add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
		
		JPanel panel3 = new JPanel();
		panel3.setBorder(new TitledBorder(new LineBorder(new Color(191, 205, 219)), "Dateien", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel3 = new GridBagConstraints();
		gbc_panel3.insets = new Insets(0, 0, 5, 0);
		gbc_panel3.fill = GridBagConstraints.BOTH;
		gbc_panel3.gridx = 0;
		gbc_panel3.gridy = 1;
		panel1.add(panel3, gbc_panel3);
		panel3.setLayout(new MigLayout("", "[][600,grow][404px]", "[14px][][14px][][][]"));
		
		JLabel lblNewLabel = new JLabel("Private Schl\u00FCssel");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel3.add(lblNewLabel, "cell 1 0,alignx left,aligny center");
		
		textField = new JTextField();
		textField.setEditable(false);
		panel3.add(textField, "cell 1 1,growx");
		textField.setColumns(10);
		
		btnNewButton = new JButton("Durchsuchen");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openPrivateKey();
			}
		});
		panel3.add(btnNewButton, "cell 2 1");
		
		JLabel lblffentlicheSchlssel = new JLabel("\u00D6ffentliche Schl\u00FCssel");
		panel3.add(lblffentlicheSchlssel, "cell 1 2,alignx left,aligny center");
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		panel3.add(textField_1, "cell 1 3,growx");
		textField_1.setColumns(10);
		
		btnNewButton_1 = new JButton("Durchsuchen");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openPublicKey();
			}
		});
		panel3.add(btnNewButton_1, "cell 2 3");
		
		JLabel lblDateien = new JLabel("Datei(en)");
		panel3.add(lblDateien, "flowy,cell 1 4,alignx left,aligny center");
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		panel3.add(textField_2, "cell 1 5,growx");
		textField_2.setColumns(10);
		
		btnNewButton_2 = new JButton("Durchsuchen");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openData();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel3.add(btnNewButton_2, "cell 2 5");
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		panel1.add(panel, gbc_panel);
		panel.setLayout(new MigLayout("", "[510][313,right]", "[][]"));
		
		JButton btnNewButton_3 = new JButton("Start");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnNewRadioButton.isSelected() == true) {
					if (rdbtnmntmAessymmetrisch.isSelected() == true) {
						Thread encryptAES = new Thread(new EncryptAES());
						encryptAES.start();
					} else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) {
						Thread encryptRSA = new Thread(new EncryptRSA());
						encryptRSA.start();
					}
				}
					
				if (rdbtnNewRadioButton_1.isSelected() == true) {
					if (rdbtnmntmAessymmetrisch.isSelected() == true) {
						Thread decryptAES = new Thread(new DecryptAES());
						decryptAES.start();
					} else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) {
						Thread decryptRSA = new Thread(new DecryptRSA());
						decryptRSA.start();
					}
				}

				if (rdbtnNewRadioButton_2.isSelected() == true) {				
					if (group_algo.getSelection() == null && group_key.getSelection() == null) {
						JOptionPane.showMessageDialog(null,"Bitte einen Algorithmus und/oder eine Schlüssel-\nlänge in den Einstellungen auwählen","Einstellungsfehler!", JOptionPane.CANCEL_OPTION);
					} else if (group_algo.getSelection() != null && group_key.getSelection() == null) {
						JOptionPane.showMessageDialog(null,"Bitte eine Schlüssellänge in den Einstellungen auswählen","Einstellungsfehler!", JOptionPane.CANCEL_OPTION); 
					} else {
						try {
							generate_keys();
						} catch (InvalidKeyException | InvalidKeySpecException
								| NoSuchPaddingException
								| IllegalBlockSizeException
								| BadPaddingException
								| InvalidAlgorithmParameterException e1) {

							e1.printStackTrace();
						}
					}
				}
			}

			private void generate_keys() throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
				KeyGenAES keyAES = new KeyGenAES();
				KeyGenRSA keyRSA = new KeyGenRSA();
				
				if (rdbtnmntmAessymmetrisch.isSelected() == true) {
					try {
						keyAES.start();
					} catch (NoSuchAlgorithmException | IOException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						keyRSA.start();
					} catch (NoSuchAlgorithmException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(btnNewButton_3, "cell 0 0,alignx right");
		
		JButton btnNewButton_4 = new JButton("Abbruch");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAll();
			}
		});
		panel.add(btnNewButton_4, "cell 1 0,alignx left");
	}
	
	public static SecretKey passwordInput(String password) {
		try {
			// Passwort verschlüsseln
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			return pass_key = keyFactory.generateSecret(keySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	protected static void resetAll() {
		int option = JOptionPane.showConfirmDialog(null, "Wollen Sie die Einstellungen zurücksetzen?", "Hinweis!", JOptionPane.YES_NO_OPTION);
		
		if (option == 0) { // Auswahl wurde bestätigt
			// Algorithmus zurücksetzen
			group_algo.clearSelection();
			rdbtnmntmAessymmetrisch.setEnabled(true);
			rdbtnmntmRsaasymmetrisch.setEnabled(true);

			// Schlüssellänge zurücksetzen
			group_key.clearSelection();
			rdbtnmntmBit.setEnabled(true);
			rdbtnmntmBit_1.setEnabled(true);
			rdbtnmntmBit_2.setEnabled(true);
			rdbtnmntmBit_3.setEnabled(true);
			rdbtnmntmBit_4.setEnabled(true);
			rdbtnmntmBit_5.setEnabled(true);
			mnSchlssellngeEinstellen.setEnabled(true);
							
			// Optionen zurücksetzen
			group_options.clearSelection();
			rdbtnNewRadioButton_2.setSelected(true);
			
			// Dateien zurücksetzen
			btnNewButton.setEnabled(false);
			btnNewButton_1.setEnabled(false);
			btnNewButton_2.setEnabled(false);
			textField.setText("");
			textField_1.setText("");
			textField_2.setText("");
		} 
	}

	protected void encrypt_data() {
		// Radio-Button betätigen
		rdbtnNewRadioButton.setSelected(true);
		
		// Schlüssellängemenü deaktivieren
		mnSchlssellngeEinstellen.setEnabled(false);
		
		// Dateien und Textfelder aktualisieren
		if (rdbtnmntmAessymmetrisch.isSelected() == true) {
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(false);
			btnNewButton_2.setEnabled(true);
			textField_1.setText(null);
			} else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) {
			btnNewButton.setEnabled(false);
			btnNewButton_1.setEnabled(true);
			btnNewButton_2.setEnabled(true);
			textField.setText(null);
			} else {
				JOptionPane.showMessageDialog(null,"Bitte einen Algorithmus in den Einstellungen auswählen!","Hinweis", JOptionPane.INFORMATION_MESSAGE);
			}
	}
	
	protected void decrypt_data() {
		// Radio-Button betätigen
		rdbtnNewRadioButton_1.setSelected(true);
		
		// Schlüssellängemenü deaktivieren
		mnSchlssellngeEinstellen.setEnabled(false);
		
		// Dateien und Textfelder aktualisieren
		if (rdbtnmntmAessymmetrisch.isSelected() == true) {
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(false);
			btnNewButton_2.setEnabled(true);
			textField_1.setText(null);
			} else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) {
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(false);
			btnNewButton_2.setEnabled(true);
			textField_1.setText(null);
			} else {
				JOptionPane.showMessageDialog(null,"Bitte einen Algorithmus in den Einstellungen auswählen!","Hinweis", JOptionPane.INFORMATION_MESSAGE);
			}
	}
	
	protected void generate_keys() {
		// Radio-Button betätigen
		rdbtnNewRadioButton_2.setSelected(true);
		
		// Textfelder aktualisieren
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");

		// Schlüssellängemenü aktivieren
		mnSchlssellngeEinstellen.setEnabled(true);
		
		btnNewButton.setEnabled(false);
		btnNewButton_1.setEnabled(false);
		btnNewButton_2.setEnabled(false);
	}

	private void openPrivateKey() { 
        JFileChooser chooser_private_key = new JFileChooser("Verzeichnis wählen");
        if (rdbtnmntmAessymmetrisch.isSelected() == true) {
        	keyDir = new File("aes-key");
        } else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) {
        	keyDir = new File("rsa-keys");
        }
        chooser_private_key.setDialogType(JFileChooser.OPEN_DIALOG); 
        chooser_private_key.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser_private_key.setCurrentDirectory(keyDir);
        
        int result = chooser_private_key.showOpenDialog(null); 

        if (result == JFileChooser.APPROVE_OPTION) { 
        	private_key = chooser_private_key.getSelectedFile();
			textField.setText(private_key.toString());
        }
	}
	
	private void openPublicKey() { 
		JFileChooser chooser_public_key = new JFileChooser("Verzeichnis wählen");
		File file = new File("rsa-keys");
		chooser_public_key.setDialogType(JFileChooser.OPEN_DIALOG); 
		chooser_public_key.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser_public_key.setCurrentDirectory(file);
	        
	    int result = chooser_public_key.showOpenDialog(null); 

	    if (result == JFileChooser.APPROVE_OPTION) { 
	    	public_key = chooser_public_key.getSelectedFile();
	    	textField_1.setText(public_key.toString());
        }
	}
	
	private void openData() throws IOException { 
        JFileChooser chooser_data = new JFileChooser("Verzeichnis wählen");
        File file_dir = new File("/home");
        if (rdbtnmntmAessymmetrisch.isSelected() == true && rdbtnNewRadioButton_1.isSelected() == true) {
        	file_dir = new File("aes-data");
        } else if (rdbtnmntmRsaasymmetrisch.isSelected() == true && rdbtnNewRadioButton_1.isSelected() == true) {
        	file_dir = new File("rsa-data");
        }
        chooser_data.setDialogType(JFileChooser.OPEN_DIALOG); 
        chooser_data.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser_data.setMultiSelectionEnabled(true);
        chooser_data.setCurrentDirectory(file_dir); 

        int result = chooser_data.showOpenDialog(null); 

        if (result == JFileChooser.APPROVE_OPTION) {
        	file = chooser_data.getSelectedFile();
			// Textfeld aktualisieren
			textField_2.setText(file.toString());
        }
	}
	
	public void saveParameter() {
	    try {
	        Properties props = new Properties();
	        if (rdbtnNewRadioButton.isSelected() == true) { props.setProperty("OptionEncrypt", String.valueOf(rdbtnNewRadioButton.isSelected())); }
	        else if (rdbtnNewRadioButton_1.isSelected() == true) { props.setProperty("OptionDecrypt", String.valueOf(rdbtnNewRadioButton_1.isSelected())); }
	        else if (rdbtnNewRadioButton_2.isSelected() == true) { props.setProperty("OptionKeyGenerate", String.valueOf(rdbtnNewRadioButton_2.isSelected())); }
	        if (textField.getText() != "") { props.setProperty("FilePrivateKey", textField.getText()); } 
	        if (textField_1.getText() != "") { props.setProperty("FilePublicKey", textField_1.getText()); }
	        if (rdbtnmntmAessymmetrisch.isSelected() == true) { props.setProperty("SetAESAlgorithm", String.valueOf(rdbtnmntmAessymmetrisch.isSelected())); }
	        else if (rdbtnmntmRsaasymmetrisch.isSelected() == true) { props.setProperty("SetRSAAlgorithm", String.valueOf(rdbtnmntmRsaasymmetrisch.isSelected())); }
	        if (rdbtnmntmBit.isSelected() == true) { props.setProperty("SetKeyLength128", String.valueOf(rdbtnmntmBit.isSelected())); }
	        else if (rdbtnmntmBit_1.isSelected() == true) { props.setProperty("SetKeyLength256", String.valueOf(rdbtnmntmBit_1.isSelected())); }
	        else if (rdbtnmntmBit_2.isSelected() == true) { props.setProperty("SetKeyLength512", String.valueOf(rdbtnmntmBit_2.isSelected())); }
	        else if (rdbtnmntmBit_3.isSelected() == true) { props.setProperty("SetKeyLength1024", String.valueOf(rdbtnmntmBit_3.isSelected())); }
	        else if (rdbtnmntmBit_4.isSelected() == true) { props.setProperty("SetKeyLength2048", String.valueOf(rdbtnmntmBit_4.isSelected())); }
	        else if (rdbtnmntmBit_5.isSelected() == true) { props.setProperty("SetKeyLength4096", String.valueOf(rdbtnmntmBit_5.isSelected())); }

	        File f = new File("crypton.properties");
	        OutputStream out = new FileOutputStream(f);
	        props.store(out, "");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}
	
	public void loadParameter() throws IOException {
	    File configFile = new File("crypton.properties");
	    
	    try {
	    	FileReader reader = new FileReader(configFile);
	    	Properties props_in = new Properties();
	    	props_in.load(reader);
	    	
	    	// Lese Optionsauswahl
	    	Boolean Encrypt = Boolean.valueOf(props_in.getProperty("OptionEncrypt"));
	    	Boolean Decrypt = Boolean.valueOf(props_in.getProperty("OptionDecrypt"));
	    	Boolean KeyGenerate = Boolean.valueOf(props_in.getProperty("OptionKeyGenerate"));
	    	
	    	// Lese Algorithmus und Keylänge
	    	Boolean SetAES = Boolean.valueOf(props_in.getProperty("SetAESAlgorithm"));
	    	Boolean SetRSA = Boolean.valueOf(props_in.getProperty("SetRSAAlgorithm"));
	       	Boolean Set128 = Boolean.valueOf(props_in.getProperty("SetAESAlgorithm"));
	    	Boolean Set256 = Boolean.valueOf(props_in.getProperty("SetKeyLength256"));
	    	Boolean Set512 = Boolean.valueOf(props_in.getProperty("SetKeyLength512"));
	    	Boolean Set1024 = Boolean.valueOf(props_in.getProperty("SetKeyLength1024"));
	    	Boolean Set2048 = Boolean.valueOf(props_in.getProperty("SetKeyLength2048"));
	    	Boolean Set4096 = Boolean.valueOf(props_in.getProperty("SetKeyLength4096"));
	    
	    	// Setze Optionsauswahl und Felder
	    	if (Encrypt == true) { 
	    		rdbtnNewRadioButton.setSelected(true); 
	    		if (SetAES ==  true) { 
	    			btnNewButton.setEnabled(true);
					btnNewButton_1.setEnabled(false);
					btnNewButton_2.setEnabled(true);
	    		} else if (SetRSA  == true) {
	    			btnNewButton.setEnabled(false);
					btnNewButton_1.setEnabled(true);
					btnNewButton_2.setEnabled(true);
	    		}
	    	}
	    	else if (Decrypt == true) { rdbtnNewRadioButton_1.setSelected(true); }	    
	    	else if (KeyGenerate == true) { rdbtnNewRadioButton_2.setSelected(true); }
	    	
	    	// Setze Textfelder von den Keys
	    	if (props_in.getProperty("FilePrivateKey") != "") { textField.setText(props_in.getProperty("FilePrivateKey")); }
	    	if (props_in.getProperty("FilePublicKey") != "") { textField_1.setText(props_in.getProperty("FilePublicKey")); }
	    
	    	// Setze Algorithmus und Keylänge
	    	if (SetAES == true) { rdbtnmntmAessymmetrisch.setSelected(true); }
	    	else if (SetRSA == true) { rdbtnmntmRsaasymmetrisch.setSelected(true); }
		
	    	if (Set128 == true) { rdbtnmntmBit.setSelected(true); }
	    	else if (Set256 == true) { rdbtnmntmBit_1.setSelected(true); }
	    	else if (Set512 == true) { rdbtnmntmBit_2.setSelected(true); }
	    	else if (Set1024 == true) { rdbtnmntmBit_3.setSelected(true); }
	    	else if (Set2048 == true) { rdbtnmntmBit_4.setSelected(true); }
	    	else if (Set4096 == true) { rdbtnmntmBit_5.setSelected(true); }
	    
	    	reader.close();
	    } 	catch (FileNotFoundException ex) {}
	    	catch (IOException ex) {}
	}
}