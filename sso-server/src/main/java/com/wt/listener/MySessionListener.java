package com.wt.listener;

import com.wt.db.MockDB;
import com.wt.pojo.ClientInfo;
import com.wt.utils.HttpUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.Map;

public class MySessionListener implements HttpSessionListener {

    //创建时候执行
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    //销毁之后执行
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //收到销毁或者自动销毁之后都会执行
        HttpSession session = httpSessionEvent.getSession();
        String token = (String) session.getAttribute("token");

        //销毁用户的信息
        MockDB.T_TOKEN.remove(token);
        //删除并拿到用户信息
        List<ClientInfo> clientInfos = MockDB.T_CLIENT_INFO.remove(token);
        //通知每一个客户端
        for (ClientInfo temp : clientInfos) {
            try {
                //遍历通知客户端注销
                HttpUtil.sendHttpRequest(temp.getClientUrl(),temp.getJsessionId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
