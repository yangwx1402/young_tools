package com.young.tools.miner.vectorized.english;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.young.tools.miner.vectorized.AbstractVectorized;
import com.young.tools.miner.vectorized.Vectorized;
import com.young.tools.miner.vectorized.analyzer.util.AnalyzerUtils;
import com.young.tools.miner.vectorized.bean.TextDoc;
import com.young.tools.miner.vectorized.bean.TextWord;
import com.young.tools.miner.vectorized.tokenized.Tokenized;

/**
 * 处理英文文本向量化
 * 
 * @author yangy
 * 
 */
public class EnglishVectorized extends AbstractVectorized implements Vectorized {

	private static final Pattern pattern = Pattern.compile("^\\w+$");

	public EnglishVectorized(Tokenized tokenized) {
		super(tokenized);
	}

	@Override
	public List<TextWord> splitWords(TextDoc doc, Analyzer analyzer)
			throws IOException {
		List<TextWord> words = new ArrayList<TextWord>();
		TokenStream ts = analyzer.tokenStream("split",
				new StringReader(doc.getDocText()));
		List<String> tempWords = AnalyzerUtils.splitWordsList(ts, pattern, 1,
				20);
		for (String temp : tempWords) {
			words.add(new TextWord(0, temp));
		}
		return words;
	}

}
