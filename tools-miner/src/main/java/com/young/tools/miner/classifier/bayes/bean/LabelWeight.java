package com.young.tools.miner.classifier.bayes.bean;

/**
 * 分类结果对象
 * @author yangy
 *
 */
public class LabelWeight implements Comparable<LabelWeight>{
    /**
     * 文档ID
     */
	private String docId;
	/**
	 * 文档类型
	 */
	private String label;
	/**
	 * 文档在该label中的评分
	 */
	private double weight;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(LabelWeight o) {
		if(this.weight>o.weight){
			return -1;
		}else if(this.weight<o.weight){
			return 1;
		}
		return 0;
	}
}
