package crypton.unit.test;

import java.security.Key;
import java.security.KeyPair;

import org.junit.Assert;
import org.junit.Test;

import crypton.CryptoException;
import crypton.KeyGenAES;
import crypton.KeyGenRSA;

public class KeyGeneratorTest {
	
	@Test
	public void generateTestAES() throws CryptoException {		
		Key key128 = KeyGenAES.generateKey(128, "AES");
		Assert.assertNotNull("Generated key is null", key128);
		
		Key key192 = KeyGenAES.generateKey(192, "AES");
		Assert.assertNotNull("Generated key is null", key192);
		
		Key key256 = KeyGenAES.generateKey(256, "AES");
		Assert.assertNotNull("Generated key is null", key256);
	}

	@Test
	public void generateTestRSA() throws CryptoException {		
		KeyPair keyPair1024 = KeyGenRSA.generateKeyPair(1024, "RSA");
		Assert.assertNotNull("Generated key pair is null", keyPair1024);
		
		KeyPair keyPair2048 = KeyGenRSA.generateKeyPair(2048, "RSA");
		Assert.assertNotNull("Generated key pair is null", keyPair2048); 		
		
		KeyPair keyPair4096 = KeyGenRSA.generateKeyPair(4096, "RSA");
		Assert.assertNotNull("Generated key pair is null", keyPair4096); 		
	}
}