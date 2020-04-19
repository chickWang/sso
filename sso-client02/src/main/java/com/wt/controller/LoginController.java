package com.wt.controller;

import com.wt.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/index")
    public String client01 (Model model) {
        model.addAttribute("logoutUrl",
                SSOClientUtil.getServerLogOutUrl());
        return "tianmao";
    }

    //注销
    @RequestMapping("/logout")
    public void logout (HttpSession session) {
        //销毁认证中心自己的session
        session.invalidate();
    }

}
