package com.wt.controller;

import com.wt.db.MockDB;
import com.wt.pojo.ClientInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class SsoServerController {

    @RequestMapping("/index")
    public String index() {
        return "login";
    }

    @RequestMapping("/login")
    //@ResponseBody //就是返回json  restController也可以实现
    public String login (String username, String password, String redirectUrl, HttpSession session, Model model) {
        System.out.println(username + "==" + password);
        //模拟数据
        if ("admin".equals(username) && "admin".equals(password)) {
            //用户名和密码正确  给用户创建一个token令牌：唯一 ，保存到数据库
            //1.生成token
            String token = UUID.randomUUID().toString();
            System.out.println("生成token成功:" + token);
            //2.保存到数据库
            MockDB.T_TOKEN.add(token);
            //3.在服务器中存放会话信息,session存放到认证服务器端跟客户端没有关系
            session.setAttribute("token",token);
            //4.返回给客户端
            model.addAttribute("token",token); //这个model添加过后重定向他是？之后的参数
            //5.认证完成，返回到自己的系统
            return "redirect:"+redirectUrl;
        }
        //登录失败
        System.out.println("用户名或者密码不正确！");
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";
    }

    //检查是否登录
    @RequestMapping("/checkLogin")
    public String checkLogin (String redirectUrl, HttpSession session, Model model) {
        //检测这个用户是否登录  就是检测是否拥有全局会话 token
        String token = (String) session.getAttribute("token");

        if (StringUtils.isEmpty(token)) {
            //没有全局的会话，跳转到登录页面,但是我从哪里来不能丢
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        } else {
            //存在全局的会话，返回来的地方（正常的逻辑要验证这个token对不对呢），要把token返回给客户端
            model.addAttribute("token",token);
            return "redirect:" + redirectUrl;
        }

    }

    //验证token是否正确
    @RequestMapping("verifyToken")
    @ResponseBody
    public String verifyToken (String token,String clientUrl,String jsessionId) {
        if (MockDB.T_TOKEN.contains(token)) {
            System.out.println("服务器端校验token成功");
            //保存用户的登出地址和sessionId
            List<ClientInfo> clientInfos = MockDB.T_CLIENT_INFO.get(token);
            if (clientInfos == null) {
                //第一次的时候还没有clientInfos这个对象  先创建一下
                clientInfos = new ArrayList<>();
                //将用户的信息放进去
                MockDB.T_CLIENT_INFO.put(token,clientInfos);
            }
            //将自己的信息  放到list里面  存到数据库里面
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setClientUrl(clientUrl);
            clientInfo.setJsessionId(jsessionId);
            clientInfos.add(clientInfo);
            return "true";
        }
        return "false";
    }

    //注销
    @RequestMapping("/logout")
    public String logout (HttpSession session) {
        //销毁认证中心自己的session
        session.invalidate();
        //通知淘宝和天猫session，监听器实现  我们如何只在这里提醒客户端
        // 就忽略了session的超时自动过期的情况了
        //session过期后  到登录页面
        return "login";
    }

}
