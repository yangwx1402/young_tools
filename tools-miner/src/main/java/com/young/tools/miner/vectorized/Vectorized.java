package com.young.tools.miner.vectorized;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;

import com.young.tools.miner.vectorized.bean.TextDoc;
import com.young.tools.miner.vectorized.bean.TextWord;
import com.young.tools.miner.vectorized.bean.WeightWord;
/**
 * 文本向量化
 * @author yangy
 *
 */
public interface Vectorized {
    /**
     * 读取文本数据
     * @param data_path
     * @param data_encoding
     * @param split_pattern
     * @return
     * @throws IOException
     */
	public List<TextDoc> readData(String data_path, String data_encoding,
			String split_pattern) throws IOException;
	/**
	 * 每个文本处理
	 * @param doc
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	public List<TextWord> splitWords(TextDoc doc,Analyzer analyzer) throws IOException;
	/**
	 * 计算tf
	 * @param docs
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	public Map<String,Map<Integer,Integer>> computeTf(List<TextDoc> docs,Analyzer analyzer) throws IOException;
	/**
	 * 计算idf
	 * @param tf
	 * @return
	 */
	public Map<Integer,Double> computeIdf(Map<String,Map<Integer,Integer>> tf);
	/**
	 * 计算tfidf
	 * @param tf
	 * @param idf
	 * @param sort
	 * @return
	 */
	public Map<String,List<WeightWord>> computeTfIdf(Map<String,Map<Integer,Integer>> tf,Map<Integer,Double> idf,boolean sort);
	/**
	 * 保存模型
	 * @param modelPath
	 * @param tf
	 * @param idf
	 * @param tfidf
	 * @throws Exception
	 */
	public abstract void saveModel(String modelPath,Map<String,Map<Integer,Integer>> tf,Map<Integer,Double> idf,Map<String,List<WeightWord>> tfidf) throws Exception;
}
