package crypton.unit.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ KeyGeneratorTest.class, PasswordGeneratorTest.class, EncryptAndDecryptTest.class })

public class AllTests {
	
}