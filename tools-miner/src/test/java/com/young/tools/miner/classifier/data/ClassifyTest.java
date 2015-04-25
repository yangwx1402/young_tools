package com.young.tools.miner.classifier.data;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;

import com.young.tools.miner.classifier.bayes.Model;
import com.young.tools.miner.classifier.bayes.ModelFactory;
import com.young.tools.miner.classifier.bayes.bean.LabelDoc;
import com.young.tools.miner.classifier.bayes.bean.LabelWeight;
import com.young.tools.miner.vectorized.analyzer.ansj.AnsjAnalysis;

public class ClassifyTest {

	public static void main(String[] args) throws Exception {
		ModelFactory factory = new ModelFactory();
		Model model = factory.loadModel("F:\\classfier_model\\model.model");
		Analyzer analyzer = new AnsjAnalysis();
		int corrctCount = 0;
		int errerCount = 0;
		int allCount = 0;
		LabelDoc doc = null;
		List<LabelWeight> labels = null;
		String l = "C000024";
		File file = new File(
				"G:\\资料\\学习\\分类\\分类器\\SogouC.reduced.20061127\\SogouC.reduced\\Reduced\\"+l);
		File[] files = file.listFiles();
		for (File f : files) {
			doc = new LabelDoc(f.getAbsolutePath(), null,
					FileUtils.readFileToString(f, "gb2312"));
			allCount++;
			labels = model.classify(doc, 1, analyzer);
			for (LabelWeight label : labels) {
				if (label.getLabel().equals(l)) {
					corrctCount++;
				} else {
					errerCount++;
				}
				System.out.println(label.getDocId() + "\t" + label.getLabel()
						+ "\t" + label.getWeight());
			}

		}
		System.out.println("allCount=" + allCount + ",correctCount="
				+ corrctCount + ",errerCount=" + errerCount);
		System.out.println(model.waitTrainCompleted());
	}
}
