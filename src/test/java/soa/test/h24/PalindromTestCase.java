package soa.test.h24;

import org.junit.Assert;
import org.junit.Test;

public class PalindromTestCase {

	@Test
	public void isPalindromeTest() {
		Assert.assertTrue(Palindrom.isPalindrome("Otto"));
	}

	@Test
	public void isNoPalindromeTest() {
		Assert.assertFalse(Palindrom.isPalindrome("Test"));
	}

	@Test
	public void isPalindromeTest1() {
		Assert.assertTrue(Palindrom.isPalindrome("Ein Neger mit Gazelle zagt im Regen nie"));
	}
	
	

}
