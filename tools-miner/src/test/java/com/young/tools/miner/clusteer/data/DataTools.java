package com.young.tools.miner.clusteer.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DataTools {

	private static final String pattern = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	public static void processNewsGroupData(String newData,String toFile) throws IOException{
		File[] files = new File(newData).listFiles();
		StringBuilder sb = new StringBuilder();
		List<String> datas = null;
		for(File f:files){
			datas = FileUtils.readLines(f);
			sb.append(f.getName()+"\t");
			for(String temp:datas){
				sb.append(temp+",");
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(new File(toFile), sb.toString());
	}
	
	public static void processTwitterData(String twitterData,String toFile) throws IOException{
		List<String> lines = FileUtils.readLines(new File(twitterData), "utf-8");
		StringBuilder sb = new StringBuilder();
		for(String line:lines){
			sb.append(line.replaceAll(pattern, "")+"\n");
		}
		FileUtils.writeStringToFile(new File(toFile), sb.toString(), "utf-8");
	}
	public static void main(String[] args) throws IOException {
//		String newData = "G:\\资料\\学习\\语料数据\\20news-18828\\20news-18828\\sci.space";
//		String toFile = "F:\\tfidf_model\\english\\englishdata.txt";
//		DataTools.processNewsGroupData(newData,toFile);
		String twitterData = "F:\\tfidf_model\\english\\english.data";
		String twitterToFile = "F:\\tfidf_model\\english\\englishdata_process.txt";
		DataTools.processTwitterData(twitterData, twitterToFile);
	}
}
