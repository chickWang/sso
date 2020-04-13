package com.wt.controller;

import com.wt.db.MockDB;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class SsoServerController {

    @RequestMapping("/index")
    public String index() {
        return "login";
    }

    @RequestMapping("/login")
    @ResponseBody //就是返回json  restController也可以实现
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
    public String verifyToken (String token) {
        if (MockDB.T_TOKEN.contains(token)) {
            System.out.println("服务器端校验token成功");
            return "true";
        }
        return "false";
    }




}
