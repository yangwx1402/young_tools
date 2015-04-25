package com.young.tools.miner.cluster.canopy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

import com.young.tools.miner.cluster.canopy.bean.Canopy;
import com.young.tools.miner.cluster.canopy.impl.CanopyCluster;
import com.young.tools.miner.vectorized.Vectorized;
import com.young.tools.miner.vectorized.analyzer.ansj.AnsjAnalysis;
import com.young.tools.miner.vectorized.bean.TextDoc;
import com.young.tools.miner.vectorized.bean.WeightWord;
import com.young.tools.miner.vectorized.english.EnglishVectorized;
import com.young.tools.miner.vectorized.tokenized.Tokenized;
import com.young.tools.miner.vectorized.tokenized.mem.MapFileTokenized;
import com.young.tools.miner.vectorized.tool.MahoutVectorTools;

public class CanopyTest {

	public static void main(String[] args) throws Exception {
		Tokenized tokenized = new MapFileTokenized("F:\\tfidf_model\\dic.txt");
		Vectorized vectorized = new EnglishVectorized(tokenized);
		List<TextDoc> docs = vectorized.readData(
				"F:\\githup-project\\yy-miner\\test_canopy\\data.txt", "utf-8",
				"\t");
		Map<String, Map<Integer, Integer>> tf = vectorized.computeTf(docs,
				new AnsjAnalysis());
		Map<Integer, Double> idf = vectorized.computeIdf(tf);
		Map<String, List<WeightWord>> tfidf = vectorized.computeTfIdf(tf,
				idf,true);
		tokenized.saveDic();
		vectorized.saveModel("F:\\tfidf_model", tf, idf, tfidf);
		MahoutVectorTools tools = new MahoutVectorTools();
		Map<String,Vector> vectors = tools.mapToMahoutVectors(tfidf);
		CanopyCluster canopy = new CanopyCluster();
		List<Canopy> canopys = canopy.runCanopy(new EuclideanDistanceMeasure(), vectors, 200.0, 100.0);
		for(Canopy c:canopys){
			System.out.print(c.getCanopyId()+"\t"+"point_size="+c.getPoints().size()+"\t");
			Iterator<Element> elements = c.getCenter().iterateNonZero();
			Element element = null;
			while(elements.hasNext()){
				element = elements.next();
				System.out.print(tokenized.getWordById(element.index())+":"+element.get()+",");
			}
			System.out.println("\n");
		}
		System.out.println(canopys.size());
		
	}
}
