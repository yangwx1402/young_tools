package com.young.tools.miner.vectorized.tokenized.mem;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.young.tools.miner.vectorized.tokenized.Tokenized;
/**
 * 已文件作为词典映射的存储实现的词典映射类
 * @author yangy
 *
 */
public class MapFileTokenized implements Tokenized {

	private int wordCount = 0;
    /**
     * 双向映射的一个Map
     */
	protected BiMap<String, Integer> words = HashBiMap.create();

	private String dicPath;

	public MapFileTokenized(String dicPath) throws Exception {
		this.dicPath = dicPath;
		if (!StringUtils.isBlank(dicPath)) {
			File file = new File(dicPath);
			if (file.exists() && file.isFile())
				initDic();
		}
	}

	private synchronized int putWord(String word) {
		if (words.containsKey(word)) {
			return words.get(word);
		} else {
			wordCount++;
			words.put(word, wordCount);
		}
		return words.get(word);
	}

	@Override
	public String getWordById(int id) {
		return words.inverse().get(id);
	}

	@Override
	public int getIdByWord(String word) {
		if (words.containsKey(word)) {
			return words.get(word);
		}
		return putWord(word);
	}

	@Override
	public void saveDic() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> entry : words.entrySet()) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		FileUtils.writeStringToFile(new File(dicPath), sb.toString(), "utf-8");
	}

	private void initDic() throws Exception {
		List<String> lines = FileUtils.readLines(new File(dicPath), "utf-8");
		String[] temp = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line))
				continue;
			temp = line.split("\t");
			words.put(temp[0], Integer.parseInt(temp[1]));
			wordCount++;
		}
	}

	@Override
	public Map<String, Integer> getWords() {
		return words;
	}

	@Override
	public int size() {
		return wordCount;
	}
}
