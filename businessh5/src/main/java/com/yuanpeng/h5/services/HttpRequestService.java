package com.yuanpeng.h5.services;

import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * @Description:
 */
@Service
public class HttpRequestService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * post调用
	 * @param url  请求地址
	 * @param params 参数
	 */
	public String post(String url, Map<String, String> params, int connectTime, int socketTime, Map<String, String> header) throws Exception {
		long startTime = System.currentTimeMillis();
		Form form = Form.form();
		for (String key : params.keySet()) {
			form.add(key, params.get(key));
		}
		Request request = Request.Post(url).bodyForm(form.build(), Consts.UTF_8).connectTimeout(connectTime).socketTimeout(socketTime);

		//header  如果有,添加
		addHeader(request, header);
		HttpResponse resp = request.execute().returnResponse();
		long endTime = System.currentTimeMillis();
		int statusCode = resp.getStatusLine().getStatusCode();
		logger.info("url={},params={},  statusCode={}, requestTime={} ms", url, params, statusCode, endTime - startTime);
		if (HttpStatus.SC_OK != statusCode) {
			throw new RuntimeException("请求失败");
		}
		String jsonData = EntityUtils.toString(resp.getEntity(), "UTF-8");
		logger.info("url={},params={}, resultJson={}, statusCode={}", url, params, jsonData, statusCode);
		return jsonData;
	}

	/**
	 * post调用
	 * @param url  请求地址
	 * @param params 参数
	 */
	public String postWithUrlEncodedForm(String url, Map<String, String> params, int connectTime, int socketTime, Map<String, String> header) throws Exception {
		long startTime = System.currentTimeMillis();
		List<NameValuePair> param = Lists.newArrayList();
		for (String key : params.keySet()) {
			param.add(new BasicNameValuePair(key, fixNull(params.get(key))));
		}
		UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(param,Consts.UTF_8);
		Request request = Request.Post(url).body(encodedFormEntity).connectTimeout(connectTime).socketTimeout(socketTime);

		//header  如果有,添加
		addHeader(request, header);
		HttpResponse resp = request.execute().returnResponse();
		long endTime = System.currentTimeMillis();
		int statusCode = resp.getStatusLine().getStatusCode();
		logger.info("url={},params={},  statusCode={}, requestTime={} ms", url, params, statusCode, endTime - startTime);
		if (HttpStatus.SC_OK != statusCode) {
			throw new RuntimeException("请求失败");
		}
		String jsonData = EntityUtils.toString(resp.getEntity(), "UTF-8");
		logger.info("url={},params={}, resultJson={}, statusCode={}", url, params, jsonData, statusCode);
		return jsonData;
	}

	/**
	 * post调用
	 * @param url  请求地址
	 * @param json 参数
	 */
	public String postWithJson(String url, String json, int connectTime, int socketTime, Map<String, String> header) throws Exception {
		long startTime = System.currentTimeMillis();
		Request request = Request.Post(url).bodyString(json, ContentType.APPLICATION_JSON).connectTimeout(connectTime).socketTimeout(socketTime);
		//header  如果有,添加
		addHeader(request, header);
		HttpResponse resp = request.execute().returnResponse();
		long endTime = System.currentTimeMillis();
		int statusCode = resp.getStatusLine().getStatusCode();
		logger.info("url={},json={},  statusCode={}, requestTime={} ms", url, json, statusCode, endTime - startTime);
		if (HttpStatus.SC_OK != statusCode) {
			throw new RuntimeException("请求失败");
		}
		String jsonData = EntityUtils.toString(resp.getEntity(), "UTF-8");
		logger.info("url={},json={}, resultJson={}, statusCode={}", url, json, jsonData, statusCode);
		return jsonData;
	}

	/**
	 * get请求
	 * @param url 请求地址
	 */
	public String get(String url, int connectTime, int socketTime, Map<String, String> header) throws Exception {
		long startTime = System.currentTimeMillis();
		Request request = Request.Post(url).connectTimeout(connectTime).socketTimeout(socketTime);
		//header  如果有,添加
		addHeader(request, header);
		HttpResponse resp = request.execute().returnResponse();
		long endTime = System.currentTimeMillis();
		int statusCode = resp.getStatusLine().getStatusCode();
		logger.info("url={},  statusCode={}, requestTime={} ms", url, statusCode, endTime - startTime);
		if (HttpStatus.SC_OK != statusCode) {
			throw new RuntimeException("请求失败");
		}
		String jsonData = EntityUtils.toString(resp.getEntity(), "UTF-8");
		logger.info("url={}, resultJson={}, statusCode={}", url, jsonData, statusCode);
		return jsonData;

	}

	/**
	 *
	 * @param request
	 * @param hm 需要加入header的信息
	 * @return
	 */
	private void addHeader(Request request, Map<String, String> hm) {
		if (hm == null || hm.size() <= 0) {
			return;
		}

		for (Map.Entry<String, String> entry : hm.entrySet()) {
			request.setHeader(entry.getKey(), entry.getValue());
		}
	}
	public static String fixNull(Object o) {
		return o == null ? "" : o.toString().trim();
	}
}
