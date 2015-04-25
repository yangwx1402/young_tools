package com.young.tools.miner.vectorized.bean;
/**
 * 权重词对象
 * @author yangy
 *
 */
public class WeightWord implements Comparable<WeightWord>{
    /**
     * ID
     */
	private int id;
	/**
	 * 词
	 */
	private String word;
	/**
	 * 权重
	 */
	private double weight;

	public WeightWord(String word, double weight) {
		this.weight = weight;
		this.word = word;
	}
	
	public WeightWord(int id,double weight){
		this.id = id;
		this.weight = weight;
	}
	
	public WeightWord(int id,String word,double weight){
		this.id = id;
		this.weight = weight;
		this.word = word;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getWord() {
		return word;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public int compareTo(WeightWord o) {
		if (this.weight > o.weight) {
			return -1;
		} else if (this.weight < o.weight) {
			return 1;
		}
		return 0;
	}

	public String toString() {
		return (word==null?id:word) + ":" + weight;
	}
}
