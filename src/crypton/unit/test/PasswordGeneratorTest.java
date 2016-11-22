package crypton.unit.test;

import javax.crypto.SecretKey;

import org.junit.Assert;
import org.junit.Test;

import crypton.CryptoException;
import crypton.CryptonApp;

public class PasswordGeneratorTest {
	
	@Test
	public void testPasswordInput() throws CryptoException{
		String text = "Very secret text to hash it";
		SecretKey pass = CryptonApp.passwordInput(text);
		Assert.assertNotNull("Generated password is null", pass); 		
	}
}