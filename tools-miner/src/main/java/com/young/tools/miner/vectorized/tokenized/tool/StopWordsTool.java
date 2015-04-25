package com.young.tools.miner.vectorized.tokenized.tool;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;
/**
 * 停用词工具
 * @author yangy
 *
 */
public class StopWordsTool {

	
	private static Set<String> readStopWords(File stopWordFile)
			throws IOException {
		Set<String> stopwords = Sets.newHashSet();
		List<String> words = null;
		words = FileUtils.readLines(stopWordFile, "utf-8");
		stopwords.addAll(words);
		return stopwords;
	}
	/**
	 * 读取停用词
	 * @param stopWordFile
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getStopWords(String stopWordFile)
			throws IOException {
		Set<String> stopwords = Sets.newHashSet();
		File file = new File(stopWordFile);
		if (file.isFile()) {
			stopwords.addAll(readStopWords(file));
		}else{
			for(File f:file.listFiles()){
				stopwords.addAll(readStopWords(f));
			}
		}
		return stopwords;
	}

}
