package com.young.tools.miner.classifier.bayes.naive;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.young.tools.common.util.threadpool.ThreadPool;
import com.young.tools.miner.classifier.bayes.Model;
import com.young.tools.miner.classifier.bayes.bean.LabelDoc;
import com.young.tools.miner.classifier.bayes.bean.LabelWeight;
import com.young.tools.miner.vectorized.analyzer.util.AnalyzerUtils;
import com.young.tools.miner.vectorized.bean.WeightWord;

/**
 * 朴素贝叶斯分类模型 P(C|F1F2...Fn)=P(F1F2...Fn|C)P(C)=P(F1|C)P(F2|C)...P(Fn|C)P(C)
 * 
 * @author yangy
 * 
 */
public class NaiveModel implements Model, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1860612606122097145L;
	/**
	 * 保存不同列别中每个词包含的文档个数
	 */
	private Map<String, Map<String, Integer>> word_document_label_counts = new HashMap<String, Map<String, Integer>>();
	/**
	 * 文档总个数
	 */
	private Integer documentCount = 0;
	/**
	 * 每个类别中文档的个数
	 */
	private Map<String, Integer> document_label_counts = new HashMap<String, Integer>();

	private transient ThreadPool threadPool;
	
	private final Pattern p = Pattern.compile("([\\u4e00-\\u9fa5]*)");

	private transient boolean completed = true;

	class ProcessDocThread implements Runnable {

		private List<LabelDoc> docs;

		private Analyzer analyzer;

		public ProcessDocThread(List<LabelDoc> docs, Analyzer analyzer) {
			this.docs = docs;
			this.analyzer = analyzer;
		}

		@Override
		public void run() {
			try {
				for (LabelDoc doc : docs) {
					countLabelDocuments(doc);
					countLabelWordDocuments(doc, analyzer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void trainModel(List<LabelDoc> trainDatas, Analyzer analyzer)
			throws Exception {
		if (threadPool == null) {
			threadPool = new ThreadPool(10, 100, 1, TimeUnit.HOURS,
					new LinkedBlockingQueue<Runnable>());
		}
		threadPool.executeThread(new ProcessDocThread(trainDatas, analyzer));
		completed = false;
	}

	/**
	 * 计算保存不同列别中每个词包含的文档个数
	 * 
	 * @param trainData
	 *            训练数据文本
	 * @param analyzer
	 *            分词器
	 * @throws IOException
	 */
	private void countLabelWordDocuments(LabelDoc trainData, Analyzer analyzer)
			throws IOException {
		TokenStream stream = analyzer.tokenStream("split", new StringReader(
				trainData.getText()));
		Set<String> words = AnalyzerUtils.splitWordsSet(stream, p, 1, 10);
		synchronized (this) {
			Map<String, Integer> wordDocumentCount = word_document_label_counts
					.get(trainData.getLabel());
			if (wordDocumentCount == null) {
				wordDocumentCount = new HashMap<String, Integer>();
			}
			for (String word : words) {
				if (wordDocumentCount.containsKey(word)) {
					wordDocumentCount
							.put(word, wordDocumentCount.get(word) + 1);
				} else {
					wordDocumentCount.put(word, 1);
				}
			}
			word_document_label_counts.put(trainData.getLabel(),
					wordDocumentCount);
		}
	}

	/**
	 * 计算不同类别中文档的个数
	 * 
	 * @param trainData
	 */
	private void countLabelDocuments(LabelDoc trainData) {
		synchronized (this) {
			documentCount++;
			if (document_label_counts.containsKey(trainData.getLabel())) {
				document_label_counts.put(trainData.getLabel(),
						document_label_counts.get(trainData.getLabel()) + 1);
			} else {
				document_label_counts.put(trainData.getLabel(), 1);
			}
		}
	}

	public List<LabelWeight> classify(LabelDoc doc, int topN, Analyzer analyzer)
			throws IOException {
		TokenStream stream = analyzer.tokenStream("split",
				new StringReader(doc.getText()));
		Set<String> words = AnalyzerUtils.splitWordsSet(stream, p, 1, 5);
		List<LabelWeight> labelWeights = new ArrayList<LabelWeight>();
		LabelWeight labelWeight = null;
		for (Map.Entry<String, Map<String, Integer>> entry : word_document_label_counts
				.entrySet()) {
			labelWeight = new LabelWeight();
			double label_p = 1.0;
			for (String word : words) {
				// 对分类概率放大200倍,为了容易观察数据
				label_p *= (entry.getValue().get(word) == null ? 1 : entry
						.getValue().get(word) * 1.0) * 200 / documentCount;
			}
			label_p *= document_label_counts.get(entry.getKey());
			labelWeight.setDocId(doc.getDocId());
			labelWeight.setLabel(entry.getKey());
			labelWeight.setWeight(label_p);
			labelWeights.add(labelWeight);
		}
		Collections.sort(labelWeights);
		if (labelWeights.size() <= topN) {
			return labelWeights;
		} else {
			return labelWeights.subList(0, topN);
		}
	}

	@Override
	public boolean waitTrainCompleted() throws Exception {
		if (threadPool != null) {
			threadPool.shutDown(false);
			threadPool.monitorThreadPool(1, TimeUnit.HOURS);
		}
		completed = true;
		threadPool = null;
		return completed;
	}

	@Override
	public List<WeightWord> getLabelFeature(String label, int topN) {
		if (!word_document_label_counts.containsKey(label))
			return null;
		List<WeightWord> words = new ArrayList<WeightWord>();
		Map<String,Integer> word_document_label_count = word_document_label_counts.get(label);
		for(Map.Entry<String, Integer> entry:word_document_label_count.entrySet()){
			words.add(new WeightWord(entry.getKey(), (entry.getValue()*1.0)/documentCount));
		}
		Collections.sort(words);
		if(words.size()<=topN){
			return words;
		}else{
			return words.subList(0, topN);
		}
	}
}
