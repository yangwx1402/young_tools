package com.young.tools.common.util.clazz;

public class ClassUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return (T) Class.forName(className).newInstance();
	}
}
