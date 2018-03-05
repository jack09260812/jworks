package com.github.utils.io;


import com.google.common.io.CharStreams;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/18.
 */
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static CloseableHttpClient httpclient;

    static {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setCharset(Consts.UTF_8)
                .build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        //请求重试处理
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        httpclient = HttpClients.custom().setConnectionManager(connManager).setConnectionManager(connManager)
                .setRetryHandler(retryHandler).setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(defaultRequestConfig).build();
    }


    /**
     * 获取httpclient
     *
     * @return
     */
    public static CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    /**
     * 根据get请求返回字符串
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String doGetAsString(String url) throws IOException {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                result = CharStreams.toString(new InputStreamReader(is, "UTF-8"));
            }
        } finally {
            if (response != null)
                response.close();
        }
        return result;
    }

    /**
     * 根据post请求返回字符串
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String doPostAsString(String url, Map<String, String> params) throws IOException {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                result = CharStreams.toString(new InputStreamReader(is, "UTF-8"));
            } else {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                result = CharStreams.toString(new InputStreamReader(is, "UTF-8"));
                logger.error("url:{},error message:{}", url, result);
            }
        } finally {
            if (response != null)
                response.close();
        }
        return result;
    }

    /**
     * 根据post请求返回字符串
     * @param proxyHost
     * @param proxyPort
     * @param proxyScheme http/https
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String doPostAsString(String proxyHost, int proxyPort, String proxyScheme, String url, Map<String, String> params) throws IOException {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyScheme);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(config);
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                result = CharStreams.toString(new InputStreamReader(is, "UTF-8"));
            } else {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                result = CharStreams.toString(new InputStreamReader(is, "UTF-8"));
                logger.error("url:{},error message:{}", url, result);
            }
        } finally {
            if (response != null)
                response.close();
        }
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */

    public static String doPostJsonAsString(String url, String json) throws IOException {
        HttpPost post = new HttpPost(url);
        String result = null;
        CloseableHttpResponse response = null;
        try {
            StringEntity se = new StringEntity(json);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(se);
            response = httpclient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(response.getEntity());
            }
        } finally {
            if (response != null)
                response.close();
        }
        return result;
    }
}