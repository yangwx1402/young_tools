package com.young.tools.miner.vectorized;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.google.common.collect.Lists;
import com.young.tools.miner.vectorized.bean.TextDoc;
import com.young.tools.miner.vectorized.bean.TextWord;
import com.young.tools.miner.vectorized.bean.WeightWord;
import com.young.tools.miner.vectorized.tokenized.Tokenized;
import com.young.tools.miner.vectorized.tokenized.mem.MapFileTokenized;
/**
 * 抽象的文本向量化类
 * @author yangy
 *
 */
public abstract class AbstractVectorized implements Vectorized{

	private int docNum;
	
	protected Tokenized tokenized;
	
	public AbstractVectorized(Tokenized tokenized){
		this.tokenized = tokenized;
	}
	
	public AbstractVectorized() throws Exception{
		this.tokenized = new MapFileTokenized(null);
	}
	/**
	 * 读入文本数据
	 * @param data_path
	 * @param data_encoding
	 * @return
	 * @throws IOException
	 */
	public List<TextDoc> readData(String data_path, String data_encoding,
			String split_pattern) throws IOException {
		List<String> dataList = FileUtils.readLines(new File(data_path),
				data_encoding);
		List<TextDoc> docs = new ArrayList<TextDoc>();
		String[] temp = null;
		for (String line : dataList) {
			if (StringUtils.isBlank(line)) {
				continue;
			}
			temp = line.split(split_pattern);
			if (temp.length >= 2) {
				docs.add(new TextDoc(temp[0], temp[1]));
			}
		}
		return docs;
	}
	/**
	 * 处理分词,这里可以添加一些个性化的处理
	 * @param doc
	 * @param analyzer
	 * @return
	 */
	public List<TextWord> splitWords(TextDoc doc,Analyzer analyzer) throws IOException{
		List<TextWord> words = new ArrayList<TextWord>();
		TokenStream ts = analyzer.tokenStream("split",
				new StringReader(doc.getDocText()));
		CharTermAttribute termAtt = null;
		String word = null;
		ts.reset();
		while (ts.incrementToken()) {
			termAtt = ts.getAttribute(CharTermAttribute.class);
			if (termAtt.length() > 1 && termAtt.length() < 20) {
				word = new String(termAtt.buffer(), 0, termAtt.length());
				words.add(new TextWord(0, word));
			}
		}
		ts.close();
		return words;
	}
	/**
	 * 计算tf
	 * @param docs
	 * @param analyzer
	 * @return
	 * @throws IOException 
	 */
	public Map<String,Map<Integer,Integer>> computeTf(List<TextDoc> docs,Analyzer analyzer) throws IOException{
		if(CollectionUtils.isEmpty(docs)){
			return null;
		}
		Map<String,Map<Integer,Integer>> tf = new HashMap<String,Map<Integer,Integer>>();
		List<TextWord> words = null;
		Map<Integer,Integer> doc_tf = null;
		for(TextDoc doc:docs){
			words = splitWords(doc, analyzer);
			if(CollectionUtils.isEmpty(words)){
				continue;
			}
			doc_tf = new HashMap<Integer,Integer>();
			for(TextWord word:words){
				if(doc_tf.containsKey(tokenized.getIdByWord(word.getWordText()))){
					doc_tf.put(tokenized.getIdByWord(word.getWordText()), doc_tf.get(tokenized.getIdByWord(word.getWordText()))+1);
				}else{
					doc_tf.put(tokenized.getIdByWord(word.getWordText()), 1);
				}
			}
			tf.put(doc.getDocId(), doc_tf);
			docNum++;
		}
		return tf;
	}
	/**
	 * 计算idf
	 */
	public Map<Integer,Double> computeIdf(Map<String,Map<Integer,Integer>> tf){
		Map<Integer,Integer> word_req_feq = new HashMap<Integer,Integer>();
		Map<Integer,Double> idf = new HashMap<Integer,Double>();
		for(Map.Entry<String, Map<Integer,Integer>> docTf:tf.entrySet()){
			for(Map.Entry<Integer, Integer> doc:docTf.getValue().entrySet()){
				if(word_req_feq.containsKey(doc.getKey())){
					word_req_feq.put(doc.getKey(), word_req_feq.get(doc.getKey())+1);
				}else{
					word_req_feq.put(doc.getKey(), 1);
				}
			}
		}
		for(Map.Entry<Integer, Integer> entry:word_req_feq.entrySet()){
			idf.put(entry.getKey(), Math.log(1.0*docNum/entry.getValue()));
		}
		return idf;
	}
	
	public Map<String,List<WeightWord>> computeTfIdf(Map<String,Map<Integer,Integer>> tf,Map<Integer,Double> idf,boolean sort){
		Map<String,List<WeightWord>> tfidf = new HashMap<String,List<WeightWord>>();
		List<WeightWord> docVector = null;
		for(Map.Entry<String, Map<Integer,Integer>> entry:tf.entrySet()){
			docVector = Lists.newArrayList();
			for(Map.Entry<Integer, Integer> wordTf:entry.getValue().entrySet()){
				docVector.add(new WeightWord(wordTf.getKey(), tokenized.getWordById(wordTf.getKey()),wordTf.getValue()*idf.get(wordTf.getKey())));
			}
			if(sort){
				Collections.sort(docVector);
			}
			tfidf.put(entry.getKey(), docVector);
		}
		return tfidf;
	}
	@Override
	public void saveModel(String modelPath,
			Map<String, Map<Integer, Integer>> tf, Map<Integer, Double> idf,
			Map<String, List<WeightWord>> tfidf) throws Exception {
		File file = new File(modelPath);
		if(!file.exists()||!file.isDirectory()){
			file.mkdirs();
		}
         StringBuilder sb = new StringBuilder();
         for(Map.Entry<String, Map<Integer,Integer>> entry:tf.entrySet()){
        	 sb.append(entry.getKey()+"\t");
        	 for(Map.Entry<Integer, Integer> docTf:entry.getValue().entrySet()){
        		 sb.append(docTf.getKey()+"-"+tokenized.getWordById(docTf.getKey())+":"+docTf.getValue()+",");
        	 }
        	 sb.append("\n");
         }
         FileUtils.writeStringToFile(new File(modelPath+File.separator+"tf.model"), sb.toString(), "utf-8");
         sb.setLength(0);
         for(Map.Entry<Integer, Double> entry:idf.entrySet()){
        	 sb.append(entry.getKey()+"-"+tokenized.getWordById(entry.getKey())+":"+entry.getValue()+"\n");
         }
         FileUtils.writeStringToFile(new File(modelPath+File.separator+"idf.model"), sb.toString(), "utf-8");
         sb.setLength(0);
         
         for(Map.Entry<String, List<WeightWord>> entry:tfidf.entrySet()){
        	 sb.append(entry.getKey()+"\t");
        	 for(WeightWord docTfidf:entry.getValue()){
        		 sb.append(docTfidf.getId()+"-"+docTfidf.getWord()+":"+docTfidf.getWeight()+",");
        	 }
        	 sb.append("\n");
         }
         FileUtils.writeStringToFile(new File(modelPath+File.separator+"tfidf.model"), sb.toString(), "utf-8");
	}
}
