package com.wt.http;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 我们的第三方的API是从京东万象上找的
 */
public class HttpTest {

    public static void main(String[] args) throws Exception {
        //1、定义需要访问的地址
        URL url = new URL("https://way.jd.com/jisuapi/query4");
        //2.连接url
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //3.设置请求方式
        connection.setRequestMethod("POST");
        //4.设置可以携带参数
        connection.setDoOutput(true);
        StringBuilder stringBuilder = new StringBuilder();
        //5.拼接请求参数
        //https://way.jd.com/jisuapi/query4?shouji=15139705191&appkey=87e293212a2ce3a15c0dbb703658e55c
        //?号可以自动拼接
        stringBuilder.append("shouji=").append("15139705191")
                .append("&appkey=").append("87e293212a2ce3a15c0dbb703658e55c");
        //6.将参数写入到url中
        connection.getOutputStream().write(stringBuilder.toString().getBytes());

        //7.发起请求
        connection.connect();
        //8.接收返回值
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        System.out.println(response);
    }

}
