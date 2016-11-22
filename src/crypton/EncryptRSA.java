package crypton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class EncryptRSA implements Runnable {
	
	static byte[] encrypted;
	static byte[] file_content;
	String messagebox;
	String algo;
	
	public void start() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		
		// Popup für Passwort von User
	    String password = (String) JOptionPane.showInputDialog(null, messagebox, "Passworteingabe", JOptionPane.WARNING_MESSAGE);
	 
	    if (password != null) {
	    	// Algorithmus setzen
	    	algo = "RSA";
	    	
	    	// Passwort verschlüsseln
	    	CryptonApp.passwordInput(password);
			byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
	    	Cipher cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
	    	PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
		  	cipher_pass.init(Cipher.DECRYPT_MODE, CryptonApp.pass_key, pbeParamSpec);
	    	
	    	// Public Key lesen und erstellen
	    	byte[] encodedPass = FileUtils.readFileToByteArray(CryptonApp.public_key);
	    	byte[] encodedKey = cipher_pass.doFinal(encodedPass);
	    	
	    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			// Einlesen der Daten
	    	// Datei lesen [einfache Methode]
	    	file_content = FileUtils.readFileToByteArray(CryptonApp.file);
    	
	    	// Dateiname einlesen und anpassen
	    	String file = CryptonApp.file.getName();
	    	String fileNameWithoutExtension = FilenameUtils.removeExtension(file);
	    	String fileExtension = FilenameUtils.getExtension(file);
		
	    	// Dateilänge und Keylänge auslesen
	    	int dataLength = file_content.length;
	    	int keyLength= (int) CryptonApp.public_key.length();
	    	
	    	if (dataLength <= keyLength) {
	    		// Verschluesseln der Daten
	    		encryptData(file_content, publicKey, algo);
					
	    		// Verzeichnis anlegen und Datei speichern
	    		File dir = new File("rsa-data");
	    		dir.mkdirs();

	    		FileOutputStream data_out = new FileOutputStream(dir.getAbsoluteFile() + "/" + fileNameWithoutExtension + "_encrypted." + fileExtension);
	    		data_out.write(encrypted);
	    		data_out.close();
					
	    		// Mitteilungsfenster
	    		JOptionPane.showMessageDialog(null,"Datei '" + fileNameWithoutExtension + "' wurde verschlüsselt","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
	    		} else {
	    			// Mitteilungsfenster
	    			JOptionPane.showMessageDialog(null,"Datei '" + fileNameWithoutExtension + "' ist zu gross.","Hinweis", JOptionPane.INFORMATION_MESSAGE);
	    		}
	    }
	}
	
	public static byte[] encryptData(byte[] file_content, PublicKey publicKey, String algo) {
		// TODO Auto-generated method stub
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
		} catch (InvalidKeyException | InvalidKeySpecException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | IOException e) {
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