/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Matteo
 */
public class HTTPUtils {

    private static Integer timeout = 10;

    public static RequestConfig buildDefaultRequestConfig(Integer timeout) {
        return RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
    }

    public static <T, S> HTTPObject POST(String url, Function<HttpPost, HttpPost> buildRequest, Function<String, T> elaboraRisposta) {
        HTTPObject res = new HTTPObject();
        try {
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(buildDefaultRequestConfig(timeout)).build();
            HttpPost request = new HttpPost(url);
            request = buildRequest.apply(request);
            HttpResponse execute = client.execute(request);
            int statuscode = execute.getStatusLine().getStatusCode();
            res.setStatus(statuscode);
            if (statuscode == 200) {
                String responseStr = EntityUtils.toString(execute.getEntity());
                res.setData(elaboraRisposta.apply(responseStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setError(e.toString());
        }
        return res;
    }

    public static <T> HTTPObject GET(String url, Function<String, T> elaboraRisposta) {
        HTTPObject res = new HTTPObject();
        try {
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(buildDefaultRequestConfig(timeout)).build();
            HttpGet loginAPI = new HttpGet(url);
            HttpResponse execute = client.execute(loginAPI);
            int statuscode = execute.getStatusLine().getStatusCode();
            res.setStatus(statuscode);
            if (statuscode == 200) {
                String responseStr = EntityUtils.toString(execute.getEntity());
                res.setData(elaboraRisposta.apply(responseStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setError(e.toString());
        }
        return res;
    }

}
