package soa.test.h24;

public class Palindrom {

	public static boolean isPalindrome(String input) {
		String a = input.toLowerCase().replaceAll("\\s+","");
		String b = new StringBuilder(a).reverse().toString();
		return a.equals(b);
	}
}
