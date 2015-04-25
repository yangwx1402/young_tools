package com.young.tools.miner.cluster.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;

import com.young.tools.miner.cluster.canopy.bean.Canopy;
import com.young.tools.miner.cluster.canopy.impl.CanopyCluster;
import com.young.tools.miner.cluster.kmeans.impl.MahoutKMeans;
import com.young.tools.miner.cluster.mahout.kmeans.Cluster;
import com.young.tools.miner.cluster.tool.ClusterDisplayTool;
import com.young.tools.miner.vectorized.Vectorized;
import com.young.tools.miner.vectorized.bean.TextDoc;
import com.young.tools.miner.vectorized.bean.WeightWord;
import com.young.tools.miner.vectorized.english.EnglishVectorized;
import com.young.tools.miner.vectorized.tokenized.Tokenized;
import com.young.tools.miner.vectorized.tokenized.mem.MapFileTokenized;
import com.young.tools.miner.vectorized.tokenized.tool.StopWordsTool;
import com.young.tools.miner.vectorized.tool.MahoutVectorTools;

public class KMeansEnglish {

	public static void main(String[] args) throws Exception {
		Tokenized tokenized = new MapFileTokenized(
				"F:\\tfidf_model\\english\\dic_english.txt");
		Vectorized vectorized = new EnglishVectorized(tokenized);
		//中文
		//Analyzer analyzer = new AnsjAnalysis();
		//英文
		Set<String> stopwords = StopWordsTool.getStopWords("F:\\maven-project\\young-tools\\tools-miner\\target\\classes\\stopword");
		CharArraySet charArraySet = new CharArraySet(Version.LUCENE_43, stopwords, true);
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_43, charArraySet);
		//中文
//		List<TextDoc> docs = vectorized.readData(
//				"F:\\githup-project\\yy-miner\\test_canopy\\data.txt", "utf-8",
//				"\t");
		//英文
		List<TextDoc> docs = vectorized.readData(
				"F:\\tfidf_model\\english\\englishdata_process.txt", "utf-8",
				"\t");
		
		Map<String, Map<Integer, Integer>> tf = vectorized.computeTf(docs,
				analyzer);
		Map<Integer, Double> idf = vectorized.computeIdf(tf);
		Map<String, List<WeightWord>> tfidf = vectorized.computeTfIdf(tf,
				idf,true);
		tokenized.saveDic();
		vectorized.saveModel("F:\\tfidf_model\\english", tf, idf, tfidf);
		MahoutVectorTools tools = new MahoutVectorTools();
		Map<String, Vector> vectors = tools.mapToMahoutVectors(tfidf);

		CanopyCluster canopy = new CanopyCluster();
		List<Canopy> canopys = canopy.runCanopy(new EuclideanDistanceMeasure(),
				vectors, 40.0, 25.0);
		MahoutKMeans kmeans = new MahoutKMeans();

		List<Vector> initCenters = new ArrayList<Vector>();
		for (Canopy c : canopys) {
			initCenters.add(c.getCenter());
		}
		vectors = tools.mapToMahoutVectors(tfidf);
		List<Vector> points = new ArrayList<Vector>();
		points.addAll(vectors.values());

		List<Cluster> clusters = kmeans.runKMeans(initCenters, points,
				new EuclideanDistanceMeasure(), 200, 0.01);
		Map<String, List<Vector>> result = kmeans.classifiPoints(clusters,
				points, new EuclideanDistanceMeasure());
		for (Map.Entry<String, List<Vector>> entry : result.entrySet()) {
			for (Vector v : entry.getValue()) {
				System.out.println(((NamedVector) v).getName() + " belong's "
						+ entry.getKey());
			}
		}
		System.out.println(points.size());
		Map<String, List<WeightWord>> clusterWords = ClusterDisplayTool
				.displayKMeans(clusters, tokenized, 10);
		for (Map.Entry<String, List<WeightWord>> entry : clusterWords
				.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}
}
