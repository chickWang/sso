package com.wt.http;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

/**
 * 发送网络请求的一个工具类
 */
public class HttpUtil {

    /**
     * 发送一个网络请求
     * @param httpUrl 请求的url
     * @param requestType GET请求还是POST请求
     * @param params 参数列表
     * @return response 请求的返回值
     * @throws Exception
     */
    public static String sendHttpRequest (String httpUrl, String requestType,
                                          Map<String,String> params) throws Exception {
        //1、定义需要访问的地址
        URL url = new URL(httpUrl);
        //2.连接url
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //3.设置请求方式
        connection.setRequestMethod(requestType);
        //4.设置可以携带参数
        connection.setDoOutput(true);
        StringBuilder stringBuilder = new StringBuilder();
        if (params != null && params.size() > 0) {
            //5.拼接请求参数
            //https://way.jd.com/jisuapi/query4?shouji=15139705191&appkey=87e293212a2ce3a15c0dbb703658e55c
            //?号可以自动拼接
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> param : entries) {
                stringBuilder.append("&").append(param.getKey())
                        .append("=").append(param.getValue());
            }
            //去除第一个&
            String p = stringBuilder.substring(1);
            //6.将参数写入到url中
            connection.getOutputStream().write(p.getBytes());
        }

        //7.发起请求
        connection.connect();
        //8.接收返回值
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        return response;
    }
}
