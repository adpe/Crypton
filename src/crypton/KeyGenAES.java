package crypton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;

public class KeyGenAES {
	
	int size = 0;
	String algo;
	String messagebox;
	static Key key;

	public void start() throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// Abfrage ob Key bereits besteht
		if (CryptonApp.rdbtnmntmBit.isSelected() == true || CryptonApp.rdbtnmntmBit_1.isSelected() == true || CryptonApp.rdbtnmntmBit_2.isSelected() == true) {
			
			// Schlüssellänge Array erstellen
			int [] size_array = {128,192,256};
			
			// Schlüssellänge abfragen
			if (CryptonApp.rdbtnmntmBit.isSelected() == true) {
			size = size_array[0];
			}
				
			if (CryptonApp.rdbtnmntmBit_1.isSelected() == true) {
			size = size_array[1];
			}
				
			if (CryptonApp.rdbtnmntmBit_2.isSelected() == true) {
			size = size_array[2];
			}
				
			// Dateiname erstellen
			File aes_file = new File("aes-key/aes" + size + ".key");
			if (aes_file.exists()) {   // Überprüfen, ob es die Datei gibt
				int option = JOptionPane.showConfirmDialog(null, "Wollen Sie den alten Schlüssel überschreiben?", "Es existiert bereits ein Schlüssel!", JOptionPane.YES_NO_OPTION);
					if (option == 0) { // Auswahl wurde bestätigt
						generateAll();
					}
				} else {
					generateAll();
				}	
			}
}

	private void generateAll() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Popup für Passwort von User
	    String password = (String) JOptionPane.showInputDialog(null, messagebox, "Passworteingabe", JOptionPane.WARNING_MESSAGE);
	 
	    if (password != null) {
			// Verzeichnis benennen und anlegen
			File dir = new File("aes-key");
			dir.mkdirs();
			
			// Algorithmus setzen
			algo = "AES";
			
			// zufaelligen AES-Key erzeugen
			generateKey(size, algo);
			 
			// Passwort verschlüsseln
			CryptonApp.passwordInput(password);
			byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
	    	Cipher cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
	    	PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
		  	cipher_pass.init(Cipher.ENCRYPT_MODE, CryptonApp.pass_key, pbeParamSpec);
			
			// Key in die Datei schreiben
			byte[] key_bytes = key.getEncoded();
			byte[] bytes = cipher_pass.doFinal(key_bytes);
			FileOutputStream keyfos = new FileOutputStream(dir.getAbsoluteFile() + "/aes" + size + ".key");
			keyfos.write(bytes);
			keyfos.close();
			JOptionPane.showMessageDialog(null,"Schlüssel 'aes" + size + "' wurde generiert","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
			}
	}
	
	public static Key generateKey(int size, String algo) {
		// zufaelligen Key erzeugen
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance(algo);
			keygen.init(size);
			return key = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}