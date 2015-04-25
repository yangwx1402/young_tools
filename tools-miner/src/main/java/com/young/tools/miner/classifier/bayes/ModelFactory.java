package com.young.tools.miner.classifier.bayes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 模型工厂
 * @author yangy
 *
 */
public class ModelFactory {
    /**
     * 保存训练完成的模型
     * @param model 模型
     * @param modelPath  保存路径
     * @throws FileNotFoundException
     * @throws IOException
     */
	public void saveModel(Model model,String modelPath) throws FileNotFoundException, IOException{
		if(model == null)
			return;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelPath));
		oos.writeObject(model);
		oos.close();
	}
	/**
	 * 加载模型
	 * @param modelPath 模型路径
	 * @return Model 模型,该模型可用于分类
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Model loadModel(String modelPath) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
		Model model = (Model) ois.readObject();
		return model;
	}
}
