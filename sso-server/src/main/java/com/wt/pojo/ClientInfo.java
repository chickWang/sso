package com.wt.pojo;

import lombok.Data;

//存储用户的一些信息
@Data
public class ClientInfo {

    private String clientUrl;//用户的登出地址
    private String jsessionId;//用户sessionId


}
