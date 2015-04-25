package com.young.tools.common.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.young.tools.common.util.http.entity.UrlFormEntity;

public class HttpClientUtils {

	private static HttpClient httpClient = HttpClients.createDefault();

	static class ResponseResult {
		private int statusCode;

		private String message;

		public ResponseResult(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getMessage() {
			return message;
		}

	}

	public static ResponseResult post(String url, Map<String, String> params,
			String encode) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		Charset charset = Charset.forName(encode);
		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(3 * 1000)
				.setSocketTimeout(5 * 1000).setConnectTimeout(5 * 1000).build();
		post.setConfig(config);
		HttpEntity entity = createUrlEncodedFormEntity(params, charset);
		if (entity != null)
			post.setEntity(entity);
		return send(post, charset);

	}

	private static ResponseResult send(HttpUriRequest request, Charset charset)
			throws ClientProtocolException, IOException {
		HttpResponse response = httpClient.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent(), charset.name()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return new ResponseResult(statusCode, sb.toString());
	}

	protected static HttpEntity createUrlEncodedFormEntity(
			Map<String, String> params, Charset charset)
			throws UnsupportedEncodingException {
		if (params == null) {
			return null;
		}
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		HttpEntity entity = new UrlFormEntity(parameters);
		return entity;
	}

	public static ResponseResult get(String url, String encode)
			throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		Charset charset = Charset.forName(encode);
		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(3 * 1000)
				.setSocketTimeout(5 * 1000).setConnectTimeout(5 * 1000).build();
		get.setConfig(config);
		return send(get, charset);
	}

}
