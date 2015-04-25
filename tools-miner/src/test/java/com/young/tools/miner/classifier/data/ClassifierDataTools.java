package com.young.tools.miner.classifier.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.young.tools.miner.classifier.bayes.bean.LabelDoc;

public class ClassifierDataTools {

	public static List<LabelDoc> processData(File f) throws IOException {
		List<LabelDoc> docs = new ArrayList<LabelDoc>();
		if (f.isDirectory()) {
			String label = f.getName();
			File[] fis = f.listFiles();
			List<String> lines = null;
			for (File temp : fis) {
				StringBuilder sb = new StringBuilder();
				lines = FileUtils.readLines(temp, "gb2312");
				for (String line : lines) {
					sb.append(line + ",");
				}
				docs.add(new LabelDoc(temp.getName(), label, sb.toString()));
			}
		}
		return docs;
	}

	public static void main(String[] args) throws IOException {
//		ClassifierDataTools
//				.processData(
//						"G:\\资料\\学习\\分类\\分类器\\SogouC.reduced.20061127\\SogouC.reduced\\Reduced",
//						"F:\\classfier_model\\data.txt");
	}
}
