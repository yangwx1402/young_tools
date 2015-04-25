package com.young.tools.miner.vectorized.bean;

import java.io.Serializable;
/**
 * 词对象
 * @author yangy
 *
 */
public class TextWord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7237428064245720200L;
    /**
     * 词ID
     */
	private int wordId;
	/**
	 * 词文本
	 */
	private String wordText;

	public TextWord(int wordId,String wordText){
		this.wordId = wordId;
		this.wordText = wordText;
	}
	
	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public String getWordText() {
		return wordText;
	}

	public void setWordText(String wordText) {
		this.wordText = wordText;
	}
}
