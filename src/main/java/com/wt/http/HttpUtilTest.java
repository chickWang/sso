package com.wt.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试封装的HttpUtil
 */
public class HttpUtilTest {

    public static void main(String[] args) throws Exception {

        Map params = new HashMap<String, String>();
        params.put("shouji","15139705191");
        params.put("appkey","87e293212a2ce3a15c0dbb703658e55c");

        //https://way.jd.com/jisuapi/query4?shouji=15139705191&appkey=87e293212a2ce3a15c0dbb703658e55c
        String response = HttpUtil.sendHttpRequest("https://way.jd.com/jisuapi/query4",
                "GET", params);

        System.out.println(response);
    }

}
