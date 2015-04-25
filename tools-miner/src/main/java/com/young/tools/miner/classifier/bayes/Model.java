package com.young.tools.miner.classifier.bayes;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import com.young.tools.miner.classifier.bayes.bean.LabelDoc;
import com.young.tools.miner.classifier.bayes.bean.LabelWeight;
import com.young.tools.miner.vectorized.bean.WeightWord;
/**
 * 分类器模型接口
 * @author yangy
 *
 */
public interface Model{

	/**
	 * 训练模型
	 * @param trainDatas 训练数据
	 * @param analyzer   分词器
	 * @throws IOException
	 */
	public void trainModel(List<LabelDoc> trainDatas, Analyzer analyzer)
			throws Exception;
    /**
     * 分类
     * @param doc 要分类的文档
     * @param topN 分类结果数据个数
     * @param analyzer 分词器
     * @return 分类结果List,根据topN的值决定返回是几个类别
     * @throws IOException
     */
	public List<LabelWeight> classify(LabelDoc doc, int topN, Analyzer analyzer)
			throws IOException;
	/**
	 * 
	 * 在保存模型前调用,确保模型训练完成
	 * @return boolean 训练是否完成
	 * @throws Exception
	 */
	public boolean waitTrainCompleted() throws Exception;
	
	public List<WeightWord> getLabelFeature(String label,int topN);
}
