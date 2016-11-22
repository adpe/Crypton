package crypton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;

public class KeyGenRSA {
	
	int size = 0;
	String algo;
	String messagebox;
	static KeyPair keyPair;

	public void start() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// Schlüssellänge Array erstellen
		int [] size_array = {1024,2048,4096};
		
		// Schlüssellänge abfragen
		if (CryptonApp.rdbtnmntmBit_3.isSelected() == true) {
			size = size_array[0];
		}
		
		if (CryptonApp.rdbtnmntmBit_4.isSelected() == true) {
			size = size_array[1];
		}
		
		if (CryptonApp.rdbtnmntmBit_5.isSelected() == true) {
			size = size_array[2];
		}

		// Abfrage ob Key bereits besteht
		if (CryptonApp.rdbtnmntmBit_3.isSelected() == true || CryptonApp.rdbtnmntmBit_4.isSelected() == true || CryptonApp.rdbtnmntmBit_5.isSelected() == true) {
			File private_file = new File("rsa-keys/private" + size + ".key");
			File public_file = new File("rsa-keys/public" + size + ".key");
					if (private_file.exists() && public_file.exists()) {   // Überprüfen, ob es die Dateien gibt
						int option = JOptionPane.showConfirmDialog(null, "Wollen Sie das alte Schlüsselpaar überschreiben?", "Es existiert bereits ein Schlüsselpaar!", JOptionPane.YES_NO_OPTION);
						
						if (option == 0) { // Auswahl wurde bestätigt
							generate();
						} 
					} else {
						generate();
					}
		}
	}

	private void generate() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// Popup für Passwort von User
	    String password = (String) JOptionPane.showInputDialog(null, messagebox, "Passworteingabe", JOptionPane.WARNING_MESSAGE);
	 
	    if (password != null) {
	    	File dir = new File("rsa-keys");
	    	dir.mkdirs();
	    	
	    	// Algorithmus setzen
	    	algo = "RSA";
	    				
	    	// zufaelliges RSA-Keypaar erzeugen
	    	generateKeyPair(size, algo);
		 
	    	// Schluessel lesen
	    	PrivateKey privateKey = keyPair.getPrivate();
	    	PublicKey publicKey = keyPair.getPublic();
		
	    	// Passwort verschlüsseln
	    	CryptonApp.passwordInput(password);
			byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
	    	Cipher cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
	    	PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
		  	cipher_pass.init(Cipher.ENCRYPT_MODE, CryptonApp.pass_key, pbeParamSpec);
		 
			// Public Key sichern
			X509EncodedKeySpec public_key = new X509EncodedKeySpec(publicKey.getEncoded());
			byte[] public_key_bytes = public_key.getEncoded();
			byte[] public_key_pass = cipher_pass.doFinal(public_key_bytes);
			FileOutputStream fos = new FileOutputStream(dir.getAbsoluteFile() + "/public" + size + ".key");
			fos.write(public_key_pass);
			fos.close();
		 
			// Private Key sichern
			PKCS8EncodedKeySpec private_key = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			byte[] private_key_bytes = private_key.getEncoded();
			byte[] private_key_pass = cipher_pass.doFinal(private_key_bytes);
			fos = new FileOutputStream(dir.getAbsoluteFile() + "/private" + size + ".key");
			fos.write(private_key_pass);
			fos.close();
			JOptionPane.showMessageDialog(null,"Schlüssel 'public" + size + "' und 'private" + size + "' wurden generiert","Fertigstellung", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	public static KeyPair generateKeyPair(int size, String algo) {
		// zufaelligen Key erzeugen
    	KeyPairGenerator keygen;
		try {
			keygen = KeyPairGenerator.getInstance(algo);
			keygen.initialize(size);
	    	return keyPair = keygen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
