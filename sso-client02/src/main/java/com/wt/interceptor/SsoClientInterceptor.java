package com.wt.interceptor;


import com.wt.utils.HttpUtil;
import com.wt.utils.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class SsoClientInterceptor implements HandlerInterceptor {

    //false 的话就是被拦截  方法执行之前   true是放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1. 判断是否存在会话  isLogin = true  （token在服务端验证完成之后，会有这个标识，就是登陆成功了）
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");

        if (isLogin != null && isLogin) {
            return true;
        }

        //2.判断token
        String token = request.getParameter("token");
        if (!StringUtils.isEmpty(token)) {
            System.out.println("获得token信息了：" + token);
            //防止伪造拿到服务器去验证
            // 服务器的地址  和  需要验证的参数
            String url = SSOClientUtil.SERVER_URL_PREFIX + "/verifyToken";
            Map<String,String> params = new HashMap<>();
            params.put("token",token);
            params.put("clientUrl",SSOClientUtil.getClientLogOutUrl());
            params.put("jsessionId",session.getId());
            try {
                String isVerify = HttpUtil.sendHttpRequest(url, "GET", params);
                if ("true".equals(isVerify)) {
                    System.out.println("服务器端检测通过！");
                    //验证通过在本地session中存一个标识
                    session.setAttribute("isLogin",true);
                    return true;
                }
            } catch (Exception e) {
                System.out.println("验证token异常");
                e.printStackTrace();
            }

        }

        //没有会话的话  跳转到统一认证中心  检测系统是否登录   认证完成之后再跳转到自己的系统
        //http://www.sso.com:8080/checkLogin?redirectUrl=http://www.tb.com:80814
        SSOClientUtil.redirectToSSOURL(request,response);
        return false;
    }

    //实现处理器后，页面渲染之前
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //页面渲染之后的回调函数
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
