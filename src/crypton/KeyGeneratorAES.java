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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;

public class KeyGeneratorAES {
	
	int length = 0;
	String messagebox;

	public void start() throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// Schlüssellänge Array erstellen
		int [] length_array = {128,192,256};
		
		// Schlüssellänge abfragen
		if (CryptonApp.rdbtnmntmBit.isSelected() == true) {
			length = length_array[0];
		}
		
		if (CryptonApp.rdbtnmntmBit_1.isSelected() == true) {
			length = length_array[1];
		}
		
		if (CryptonApp.rdbtnmntmBit_2.isSelected() == true) {
			length = length_array[2];
		}
		
		// Abfrage ob Key bereits besteht
		if (CryptonApp.rdbtnmntmBit.isSelected() == true || CryptonApp.rdbtnmntmBit_1.isSelected() == true || CryptonApp.rdbtnmntmBit_2.isSelected() == true) {
			File aes_file = new File("aes-key/aes" + length + ".key");
			if (aes_file.exists()) {   // Überprüfen, ob es die Datei gibt
				int option = JOptionPane.showConfirmDialog(null, "Wollen Sie den alten Schlüssel überschreiben?", "Es existiert bereits ein Schlüssel!", JOptionPane.YES_NO_OPTION);
				
				if (option == 0) { // Auswahl wurde bestätigt
					generate();
				} 
			} else {
				generate();
			}
	}
}

	private void generate() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Popup für Passwort von User
	    String password = (String) JOptionPane.showInputDialog(null, messagebox, "Passworteingabe", JOptionPane.WARNING_MESSAGE);
	 
	    if (password != null) {
			// Verzeichnis benennen und anlegen
			File dir = new File("aes-key");
			dir.mkdirs();
			 
			// zufaelligen Key erzeugen
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(length);
			Key key = keygen.generateKey();
			
			// Passwort verschlüsseln
			byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
	        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	        SecretKey pass_key = keyFactory.generateSecret(keySpec);

	        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
	        Cipher cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
	        cipher_pass.init(Cipher.ENCRYPT_MODE, pass_key, pbeParamSpec);
			 
			// Key in die Datei schreiben
			byte[] key_bytes = key.getEncoded();
			byte[] bytes = cipher_pass.doFinal(key_bytes);
			FileOutputStream keyfos = new FileOutputStream(dir.getAbsoluteFile() + "/aes" + length + ".key");
			keyfos.write(bytes);
			keyfos.close();
			JOptionPane.showMessageDialog(null,"Schlüssel 'aes" + length + "' wurde generiert","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
	    } else {
	    	System.out.println("no password set");
	    }
	}
}