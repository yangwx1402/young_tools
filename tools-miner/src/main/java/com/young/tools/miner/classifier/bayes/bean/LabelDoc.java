package com.young.tools.miner.classifier.bayes.bean;
/**
 * 分类文档对象
 * @author yangy
 *
 */
public class LabelDoc {
	
	public LabelDoc(String docId,String label,String text){
		this.docId = docId;
		this.label = label;
		this.text = text;
	}
	
	public LabelDoc(){}
	/**
	 * 文档ID
	 */
	private String docId;
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * 训练数据中的文档类别
	 */
	private String label;
    /**
     * 文档内容
     */
	private String text;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
