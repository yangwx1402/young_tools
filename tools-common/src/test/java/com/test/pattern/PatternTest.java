package com.test.pattern;

import java.util.regex.Pattern;

public class PatternTest {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,4}$");
	}
}
