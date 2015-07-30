package soa.test.h24;

import static org.junit.Assert.*;

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

}
