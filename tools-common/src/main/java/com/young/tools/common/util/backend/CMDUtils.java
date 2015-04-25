package com.young.tools.common.util.backend;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CMDUtils {

	public static List<String> executeCommand(String command) throws IOException{
		Process process = Runtime.getRuntime().exec(command);
		return IOUtils.readLines(process.getInputStream(), "utf-8");
	}
	
	public static void printResult(List<String> list){
		for(String str:list){
			System.out.println(str);
		}
	}
	
	public static void main(String[] args) throws IOException {
		List<String> list = CMDUtils.executeCommand("ls -ltr");
		printResult(list);
	}
}
