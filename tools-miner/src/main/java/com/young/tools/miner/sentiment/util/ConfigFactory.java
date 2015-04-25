package com.young.tools.miner.sentiment.util;


/**
 * 单例模式config
 * @author yangyong
 *
 */
public class ConfigFactory {

    private static Config config = new Config("classpath*:sentiment/sentiment.properties");
    
    public static Config config() {
        return config;
    }
}