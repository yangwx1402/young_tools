package com.young.tools.miner.pattern;

public class PatternTest {

	public static void main(String[] args) {
		String pattern = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		
		System.out.println("http://t.co/h7bTwUBaj7".matches(pattern));
		System.out.println("Collider hopes for a 'super' restart http://t.co/h7bTwUBaj7".replaceAll(pattern, ""));
	}
}
