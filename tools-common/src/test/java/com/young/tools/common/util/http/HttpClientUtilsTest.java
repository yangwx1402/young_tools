package com.young.tools.common.util.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.young.tools.common.util.http.HttpClientUtils.ResponseResult;

public class HttpClientUtilsTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		ResponseResult result = HttpClientUtils.get("http://www.sina.com.cn", "gb2312");
		System.out.println(result.getMessage());
		System.out.println(result.getStatusCode());
	}
}
