package com.young.tools.miner.vectorized.tokenized;

import java.util.Map;
/**
 * 词映射对象类
 * @author yangy
 *
 */
public interface Tokenized {
    /**
     * 根据Id获取词
     * @param id
     * @return
     */
	public String getWordById(int id);
    /**
     * 根据词获取词ID
     * @param word
     * @return
     */
	public int getIdByWord(String word);
	/**
	 * 保存词典映射关系
	 * @throws Exception
	 */
	public void saveDic() throws Exception;
	/**
	 * 获取词和ID的映射关系
	 * @return
	 */
	public Map<String,Integer> getWords();
	/**
	 * 获取词典的个数
	 * @return
	 */
	public int size();
}
