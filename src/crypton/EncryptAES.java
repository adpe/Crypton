package crypton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncryptAES implements Runnable {
	
	static byte[] encrypted;
	static byte[] file_content;
	public static char[] bytesFile;
	String messagebox;
	String algo;
	SecretKey key = null;

	public void start() throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InterruptedException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		
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
	
	    	// Verschluesseln der Daten
	    	encryptData(file_content, key, algo);

	    	// Verzeichnis anlegen und Datei speichern
	    	File dir = new File("aes-data");
	    	dir.mkdirs();

	    	FileOutputStream data_out = new FileOutputStream(dir.getAbsoluteFile() + "/" + fileNameWithoutExtension + "_encrypted." + fileExtension);
	    	data_out.write(encrypted);
	    	data_out.close();
		
	    	// Mitteilungsfenster
	    	JOptionPane.showMessageDialog(null,"Datei '" + fileNameWithoutExtension + "' wurde verschlüsselt","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
	    	
	    }
	}

	public static byte[] encryptData(byte[] file_content, SecretKey key, String algo) {
		// TODO Auto-generated method stub
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return encrypted = cipher.doFinal(file_content);
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
			CryptonApp.resetAll();
		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException | InterruptedException e) {
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