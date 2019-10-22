package com.xingyun.equipment.crane.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjy
 * @date 2018/5/29
 * 短信发送工具类
 */
@SuppressWarnings("all")
public class SMSUtils {


    /*public static void main(String[] args) {
        try {
            SMSUtils.sendSMSMessage("15123352688,15206122636","测试短信");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 发生短信验证码  说明:name和pwd 由向服务商申请获得
     *
     * @param mobile 多个以逗号隔开
     * @param message  例如: 注册验证码：123456，请在5分钟内填写。如非本人操作，请忽略。
     * @return
     * @throws Exception
     */
    public static String sendSMSMessage(String mobile, String message) throws Exception {
        // 服务器地址
        String url = "http://web.1xinxi.cn/asmx/smsservice.aspx";

        // 参数赋值
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", "laowt"));
        params.add(new BasicNameValuePair("pwd", "8A425F48E1606018F4FBE1CBD737"));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("content", message));
        params.add(new BasicNameValuePair("stime", ""));
        params.add(new BasicNameValuePair("type", "pt"));
        params.add(new BasicNameValuePair("action", "send"));
        params.add(new BasicNameValuePair("sign", "【星云网格智慧工地】"));
        return request(url, params);
    }

    /**
     * @param url
     * @param params
     * @param authUser
     * @param authPwd
     * @return
     * @throws Exception
     */
    public static String request(String url, List<NameValuePair> params, String authUser, String authPwd) throws Exception {
        CloseableHttpClient client = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity);
            byte[] byte64 = org.apache.commons.codec.binary.Base64.encodeBase64((authUser + ":" + authPwd).getBytes());
            String aut = "Basic " + new String(byte64);
            httpPost.setHeader("Authorization", aut);
            client = HttpClients.custom().build();
            HttpResponse response = client.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            httpPost.abort();
            client.close();
        }
        return "";
    }

    public static String request(String url, List<NameValuePair> params) throws Exception {

        // 结果
        String result = null;

        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
        httppost.setEntity(uefEntity);

        // 设置请求与数据处理的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(20000).build();
        httppost.setConfig(requestConfig);

        // 提交请求
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity, "UTF-8");
        } else {
            httppost.releaseConnection();
            throw new Exception();
        }
        httppost.releaseConnection();
        return result;
    }

    public static String request(String url, List<NameValuePair> params, String token) throws Exception {
        CloseableHttpClient client = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("token", token);
            client = HttpClients.custom().build();
            HttpResponse response = client.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
            httpPost.abort();
            client.close();
        }
        return "";
    }


}
