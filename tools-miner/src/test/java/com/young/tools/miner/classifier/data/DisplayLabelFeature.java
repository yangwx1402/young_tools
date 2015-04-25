package com.young.tools.miner.classifier.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.young.tools.miner.classifier.bayes.Model;
import com.young.tools.miner.classifier.bayes.ModelFactory;
import com.young.tools.miner.vectorized.bean.WeightWord;

public class DisplayLabelFeature {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		ModelFactory factory = new ModelFactory();
		Model model = factory.loadModel("F:\\classfier_model\\model.model");
		List<WeightWord> C000014 = model.getLabelFeature("C000016", 20);
		List<WeightWord> C000023 = model.getLabelFeature("C000023", 20);
		System.out.println(C000014);
		System.out.println(C000023);
	}
}
