package com.web.common.web.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP网络请求
 *
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月23日 下午7:02:11
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static void main(String args[]) {
    }

    /**
     * 获取采用HTTPS协议的HTTP客户端
     *
     * @return
     */
    public static CloseableHttpClient getHttpsClient() {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
            SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> r =
            RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf).build();
        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 创建一个HTTP响应的处理器
     */
    private static final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        /**
         * 对HTTP响应进行处理
         */
        public String handleResponse(final HttpResponse response)
            throws ClientProtocolException, IOException {
            // 获取HTTP响应状态码
            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                // 返回的状态码表示成功，则解析响应实体
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return null;
                }
                String content = EntityUtils.toString(entity);
                if (logger.isDebugEnabled()) {
                    logger.debug(content);
                }
                return content;
            }
            return null;
        }
    };

    /**
     * 根据指定的参数集合及编码集来构建HTTP GET方式的查询参数字符串
     *
     * @param params  参数集合
     * @param charset 指定的编码集
     * @return 构建成功后的形如“p1=v1&p2=v2&p3=v3”的查询参数字符串
     * @throws UnsupportedEncodingException 编码数据错误时抛出
     */
    private static String buildQuery(Map<String, String> params, String charset)
        throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        if (entries != null && entries.size() > 0) {
            for (Entry<String, String> entry : entries) {
                String name = entry.getKey();
                String value = entry.getValue() == null ? "" : entry.getValue().trim();
                // 忽略参数名或参数值为空的参数
                if (StringUtils.isEmpty(entry.getKey()) && StringUtils.isEmpty(entry.getValue())) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }
        }
        return query.toString();
    }

    /**
     * 处理HTTP GET请求
     *
     * @param httpClient    Http客户端
     * @param requestConfig HTTP请求配置信息
     * @param params        请求参数集合
     * @param uri           相对路径
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     */
    public static String doGet(CloseableHttpClient httpClient, RequestConfig requestConfig,
        Map<String, String> params, String uri, String charset)
        throws ClientProtocolException, IOException {
        return doGet(httpClient, requestConfig, params, null, uri, charset);
    }

    /**
     * 处理HTTP GET请求
     *
     * @param requestConfig HTTP请求配置信息
     * @param params        请求参数集合
     * @param uri           相对路径
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     */
    public static String doGet(RequestConfig requestConfig, Map<String, String> params, String uri,
        String charset) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return doGet(httpClient, requestConfig, params, null, uri, charset);
    }

    /**
     * 处理HTTP GET请求
     *
     * @param httpClient    Http客户端
     * @param requestConfig HTTP请求配置信息
     * @param params        请求参数集合
     * @param headers       HTTP请求头字段集合
     * @param uri           相对路径
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     */
    public static String doGet(CloseableHttpClient httpClient, RequestConfig requestConfig,
        Map<String, String> params, Map<String, String> headers, String uri, String charset)
        throws ClientProtocolException, IOException {
        String responseBody = null;
        // 构建查询参数
        String query = buildQuery(params, charset);
        if (!StringUtils.isEmpty(query)) {
            uri = uri + "?" + query;
        }
        // 创建HttpGet实例
        HttpGet httpGet = new HttpGet(uri);

        // 设置HTTP请求配置信息
        httpGet.setConfig(requestConfig);

        // 设置请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        } else { // 默认的请求头
            httpGet.addHeader("text/plain", charset);
        }

        // 执行Http Get请求并获取响应
        responseBody = httpClient.execute(httpGet, responseHandler);
        if (httpClient != null) {
            httpClient.close();
        }
        return responseBody;
    }

    /**
     * 处理HTTP POST请求
     *
     * @param httpClient    Http客户端
     * @param requestConfig HTTP请求配置信息
     * @param params        请求参数集合
     * @param headers       HTTP请求头字段集合
     * @param uri           相对路径
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     */
    public static String doPost(CloseableHttpClient httpClient, RequestConfig requestConfig,
        Map<String, String> params, Map<String, String> headers, String uri, String charset)
        throws ClientProtocolException, IOException {
        List<BasicNameValuePair> nvs = new ArrayList<BasicNameValuePair>(2);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isEmpty(entry.getKey()) && StringUtils.isEmpty(entry.getValue())) {
                    String key = entry.getKey();
                    String value = entry.getValue() == null ? "" : entry.getValue().trim();
                    nvs.add(new BasicNameValuePair(key, value));
                }
            }
        }
        HttpEntity httpEntity = new UrlEncodedFormEntity(nvs, charset);
        return doPostWithEntity(httpClient, requestConfig, headers, httpEntity, uri, charset);
    }

    /**
     * 处理HTTP POST请求
     *
     * @param requestConfig HTTP请求配置信息
     * @param params        请求参数集合
     * @param headers       HTTP请求头字段集合
     * @param uri           相对路径
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     */
    public static String doPost(RequestConfig requestConfig, Map<String, String> params,
        Map<String, String> headers, String uri, String charset)
        throws ClientProtocolException, IOException {
        List<BasicNameValuePair> nvs = new ArrayList<BasicNameValuePair>(2);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isEmpty(entry.getKey()) && StringUtils.isEmpty(entry.getValue())) {
                    String key = entry.getKey();
                    String value = entry.getValue() == null ? "" : entry.getValue().trim();
                    nvs.add(new BasicNameValuePair(key, value));
                }
            }
        }
        HttpEntity httpEntity = new UrlEncodedFormEntity(nvs, charset);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return doPostWithEntity(httpClient, requestConfig, headers, httpEntity, uri, charset);
    }

    /**
     * 处理HTTP POST请求,并关闭客户端
     *
     * @param httpClient    Http客户端
     * @param requestConfig HTTP请求配置信息
     * @param headers       HTTP请求头字段集合
     * @param httpEntity    HTTP请求体
     * @param uri
     * @param charset       字符集
     * @return 如果请求被正常执行，则返回服务器返回的JSON格式的响应数据；<br>
     * 如果出现异常，则返回JSON格式的异常描述信息。
     * @throws IOException             网络异常或编码数据异常时抛出
     * @throws ClientProtocolException HTTP协议错误时抛出
     * @throws ApiException            当服务器端返回的HTTP状态码不为2XX时抛出
     */
    public static String doPostWithEntity(CloseableHttpClient httpClient,
        RequestConfig requestConfig, Map<String, String> headers, HttpEntity httpEntity, String uri,
        String charset) throws ClientProtocolException, IOException {
        String resp = null;
        // 创建Http Post实例
        HttpPost httpPost = new HttpPost(uri);

        // 设置HTTP请求配置信息
        httpPost.setConfig(requestConfig);

        // 设置请求实体
        httpPost.setEntity(httpEntity);

        // 设置请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        } else { // 默认的请求头
            httpPost.addHeader("text/plain", charset);
        }

        // 执行Http Post请求并获取响应
        resp = httpClient.execute(httpPost, responseHandler);

        if (httpClient != null) {
            httpClient.close();
        }
        return resp;
    }
}
