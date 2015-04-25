package com.young.tools.miner.classifier.data;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.young.tools.miner.classifier.bayes.Model;
import com.young.tools.miner.classifier.bayes.ModelFactory;
import com.young.tools.miner.classifier.bayes.bean.LabelDoc;
import com.young.tools.miner.classifier.bayes.naive.NaiveModel;
import com.young.tools.miner.vectorized.analyzer.ansj.AnsjAnalysis;

public class ClassifierTrainTest {

	public static void main(String[] args) throws Exception {
		Model model = new NaiveModel();
		Analyzer analyzer = new AnsjAnalysis();
		File file = new File("G:\\资料\\学习\\分类\\分类器\\SogouC.reduced.20061127\\SogouC.reduced\\Reduced");
		File[] files = file.listFiles();
		List<LabelDoc> docs = null;
		for(File f:files){
			docs = ClassifierDataTools.processData(f);
			model.trainModel(docs,analyzer);
		}
		System.out.println(model.waitTrainCompleted());
		System.out.println(model.waitTrainCompleted());
		System.out.println(model.waitTrainCompleted());
		ModelFactory factory = new ModelFactory();
		factory.saveModel(model, "F:\\classfier_model\\model.model");
		
	}
}
