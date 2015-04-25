package com.young.tools.miner.vectorized.bean;

import java.io.Serializable;
/**
 * 文本文档类
 * @author yangy
 *
 */
public class TextDoc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3602827498006132569L;
    /**
     * 文档ID
     */
	private String docId;
    /**
     * 文档文本
     */
	private String docText;

	public TextDoc() {
	}

	public TextDoc(String docId, String docText) {
		this.docId = docId;
		this.docText = docText;

	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDocText() {
		return docText;
	}

	public void setDocText(String docText) {
		this.docText = docText;
	}
	
	public String toString(){
		return docId+"\t"+docText;
	}

}
