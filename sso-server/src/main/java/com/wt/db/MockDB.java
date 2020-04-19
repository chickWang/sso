package com.wt.db;

import com.wt.pojo.ClientInfo;

import java.util.*;

/**
 * 模拟数据库
 */
public class MockDB {

    // token 要求无序不重复
    public static Set<String> T_TOKEN = new HashSet<>();

    //保存用户注销是需要的相应的信息   key为token
    public static Map<String, List<ClientInfo>> T_CLIENT_INFO =
            new HashMap<>();
}
