package crypton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class DecryptAES implements Runnable {
	
	static byte[] decrypted;
	static byte[] file_content;
	String messagebox;
	String algo;
	SecretKey key;
	
	public void start() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		// Popup für Passwort von User
	    String password = (String) JOptionPane.showInputDialog(null, messagebox, "Passworteingabe", JOptionPane.WARNING_MESSAGE);
	 
	    if (password != null) {
	    	// Algorithmus setzen
	    	algo = "AES";

			// Passwort verschlüsseln
	    	CryptonApp.passwordInput(password);
			byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
	    	Cipher cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
	    	PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
		  	cipher_pass.init(Cipher.DECRYPT_MODE, CryptonApp.pass_key, pbeParamSpec);
			
	    	// Key Datei lesen und erstellen
			byte[] encodedPass = FileUtils.readFileToByteArray(CryptonApp.private_key);
	    	byte[] encodedKey = cipher_pass.doFinal(encodedPass);
	    	key = new SecretKeySpec(encodedKey, "AES"); 
			
	    	// Einlesen der Daten
	    	// Datei lesen [einfache Methode]
	    	file_content = FileUtils.readFileToByteArray(CryptonApp.file);
	    	
	    	// Dateiname einlesen und anpassen
	    	String file = CryptonApp.file.getName();
	    	String fileNameWithoutExtension = FilenameUtils.removeExtension(file);
	    	String fileExtension = FilenameUtils.getExtension(file);
			String blankFile = file.replace("_encrypted." + fileExtension,"");
		
			// Entschluesseln der Daten
			decryptData(file_content, key, algo);
		
			// Verzeichnis anlegen und Datei abspeichern
			File dir = new File("aes-data");
			dir.mkdirs();
		
			FileOutputStream data_out = new FileOutputStream(dir.getAbsoluteFile() + "/" + blankFile + "_decrypted." + fileExtension);
			data_out.write(decrypted);
			data_out.close();
			
			// Mitteilungsfenster
			JOptionPane.showMessageDialog(null,"Datei '" + fileNameWithoutExtension +  "' wurde entschlüsselt","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	public static byte[] decryptData(byte[] file_content, SecretKey key, String algo) {
		// TODO Auto-generated method stub
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return decrypted = cipher.doFinal(file_content);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			start();
		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Möglicherweise wurde das falsche Passwort eingegeben");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			System.out.println("Möglicherweise wurde das falsche Passwort eingegeben");
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			System.out.println("Möglicherweise wurde das falsche Passwort eingegeben");
			e.printStackTrace();
		}
	}
}