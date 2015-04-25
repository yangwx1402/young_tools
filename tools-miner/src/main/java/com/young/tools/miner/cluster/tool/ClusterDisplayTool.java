package com.young.tools.miner.cluster.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mahout.math.Vector.Element;

import com.google.common.collect.Lists;
import com.young.tools.miner.cluster.mahout.kmeans.Cluster;
import com.young.tools.miner.vectorized.bean.WeightWord;
import com.young.tools.miner.vectorized.tokenized.Tokenized;
/**
 * 聚类结果展示工具
 * @author yangy
 *
 */
public class ClusterDisplayTool {

	public static Map<String, List<WeightWord>> displayKMeans(
			List<Cluster> clusters, Tokenized tokenized,int topN) {
		Map<String, List<WeightWord>> words = new HashMap<String, List<WeightWord>>();
		for (Cluster cluster : clusters) {
			words.put(cluster.getIdentifier(),
					displayKMeans(cluster, tokenized,topN));
		}
		return words;
	}

	public static List<WeightWord> displayKMeans(Cluster cluster,
			Tokenized tokenized,int topN) {
		List<WeightWord> words = Lists.newArrayList();
		Iterator<Element> it = cluster.getCenter().iterateNonZero();
		Element element = null;
		while (it.hasNext()) {
			element = it.next();
			words.add(new WeightWord(tokenized.getWordById(element.index()),
					element.get()));
		}
		Collections.sort(words);
		if(words.size()<=topN){
			return words;
		}else{
			return words.subList(0, topN);
		}
	}
}
