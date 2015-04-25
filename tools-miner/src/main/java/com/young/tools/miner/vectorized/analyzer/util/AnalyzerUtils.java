package com.young.tools.miner.vectorized.analyzer.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.google.common.collect.Lists;

/**
 * 分词处理
 * 
 * @author yangy
 * 
 */
public class AnalyzerUtils {

	/**
	 * 分词处理
	 * 
	 * @param ts
	 * @return
	 * @throws IOException
	 */
	public static List<String> splitWordsList(TokenStream ts, Pattern p,
			int min_limit, int max_limit) throws IOException {
		List<String> words = Lists.newArrayList();
		CharTermAttribute termAtt = null;
		String word = null;
		ts.reset();
		Matcher m = null;
		boolean r = true;
		while (ts.incrementToken()) {
			termAtt = ts.getAttribute(CharTermAttribute.class);
			word = new String(termAtt.buffer(), 0, termAtt.length());
			if (word.length() > min_limit && word.length() < max_limit) {
				if (p != null) {
					m = p.matcher(word);
					r = m.matches();
				}
				if (r) {
					words.add(word);
				}
			}
		}
		ts.close();
		return words;
	}

	public static Set<String> splitWordsSet(TokenStream ts, Pattern p,
			int min_limit, int max_limit) throws IOException {
		Set<String> words = new HashSet<String>();
		words.addAll(splitWordsList(ts, p, min_limit, max_limit));
		return words;
	}
}
