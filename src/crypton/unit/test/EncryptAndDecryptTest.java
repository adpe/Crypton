package crypton.unit.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.junit.Test;

import crypton.CryptoException;
import crypton.CryptonApp;
import crypton.DecryptAES;
import crypton.EncryptAES;
import crypton.KeyGenAES;


public class EncryptAndDecryptTest {
	
	SecretKey final_key;
	
	// Test-Input
	String eingabe = "Dies ist bloss ein Testext";
	byte[] text = eingabe.getBytes();
	
	// Algorithmus setzen
	String algo = "AES";

	
	// Password-Input
	SecretKey pass_key = CryptonApp.passwordInput("GebenSieBitteIhrPasswortEin");
  	
  	// Zufaelligen AES Key erzeugen
	Key key = KeyGenAES.generateKey(128, algo);
	
	@Test
	public void encryptAndDecryptTestAES() throws CryptoException {
		System.out.println("AES Algorithmus");
		System.out.println("----------------");
		System.out.println("Eingabe: " + eingabe);
		generatePasswordKey();
		byte[] encrypted = EncryptAES.encryptData(text, final_key, algo);
		System.out.println("Verschlüsselte Text: " + new String(encrypted));
		byte[] decrypted = DecryptAES.decryptData(encrypted, final_key, algo);
		System.out.println("Entschlüsselte Text: " + new String(decrypted));
		Assert.assertTrue("Decrypted text does not match expected", java.util.Arrays.equals(text, decrypted));
	}
	
	private SecretKey generatePasswordKey() {
		byte[] salt = {(byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,(byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17};
		Cipher cipher_pass;
		try {
			cipher_pass = Cipher.getInstance("PBEWithMD5AndDES");
			PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
			cipher_pass.init(Cipher.ENCRYPT_MODE, pass_key, pbeParamSpec);
		  	byte[] key_bytes = key.getEncoded();
			byte[] encodedKey = cipher_pass.doFinal(key_bytes);
			return final_key = new SecretKeySpec(encodedKey, algo); 
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidAlgorithmParameterException e) {
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
}