package com.young.tools.miner.vectorized.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

import com.young.tools.miner.vectorized.bean.WeightWord;
/**
 * Mahout 向量工具
 * @author yangy
 *
 */
public class MahoutVectorTools {

	static class Entry{
		
		private String idOrName;
		
		private Map<Integer,Double> vector;
		
		public Entry(String idOrName,Map<Integer,Double> vector){
			this.idOrName = idOrName;
			this.vector = vector;
		}

		public String getIdOrName() {
			return idOrName;
		}

		public void setIdOrName(String idOrName) {
			this.idOrName = idOrName;
		}

		public Map<Integer, Double> getVector() {
			return vector;
		}

		public void setVector(Map<Integer, Double> vector) {
			this.vector = vector;
		}
	}
	
	public Map<String,Vector> mapToMahoutVectors(Map<String,List<WeightWord>> mapVectors){
		Map<String,Vector> vectors = new HashMap<String,Vector>();
		Vector nameVector = null;
		for(Map.Entry<String, List<WeightWord>> entry:mapVectors.entrySet()){
			nameVector = mapToMahoutVector(entry.getKey(), entry.getValue());
			vectors.put(entry.getKey(), nameVector);
		}
		return vectors;
	}
	
	public Vector mapToMahoutVector(String idOrName,List<WeightWord> mapVector){
		Vector vector = new RandomAccessSparseVector(Integer.MAX_VALUE);
		for(WeightWord entry:mapVector){
			vector.set(entry.getId(), entry.getWeight());
		}
		return new NamedVector(vector, idOrName);
	}
	
	public Entry mahoutVectorToMap(String idOrName,Vector vector){
		Map<Integer,Double> mapVector = new HashMap<Integer,Double>();
		Iterator<Element> it = vector.iterateNonZero();
		Element element = null;
		while(it.hasNext()){
			element = it.next();
			mapVector.put(element.index(), element.get());
		}
		return new Entry(idOrName,mapVector);
	}
	
	public Map<String,Map<Integer,Double>> mahoutVectorToMaps(List<Vector> vectors){
		Map<String,Map<Integer,Double>> mapVectors = new HashMap<String,Map<Integer,Double>>();
		NamedVector nameVector = null;
		Entry entry = null;
		for(Vector v:vectors){
			nameVector = (NamedVector)v;
			entry = mahoutVectorToMap(nameVector.getName(), nameVector.getDelegate());
			mapVectors.put(entry.getIdOrName(), entry.getVector());
		}
		return mapVectors;
	}
}
