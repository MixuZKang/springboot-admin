package com.mixu.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*使用拦截器进行登录检查
    1、配置好拦截器要拦截哪些请求，以前使用xml配置，现在所有SpringMVC的定制化操作都可以
        通过实现WebMvcConfigurer来定制
    2、把这些配置放在容器中

  拦截器原理：
    1、根据当前请求，找到HandlerExecutionChain【可以处理请求的handler以及handler的所有 拦截器】
    2、先来执行所有拦截器的 preHandle方法【顺序】
      1.如果当前拦截器preHandle返回为true。则执行下一个拦截器的preHandle
      2.如果当前拦截器返回为false。直接执行所有已经执行了的拦截器的 afterCompletion【倒序】
    3、如果任何一个拦截器返回false。直接跳出，不执行目标方法
    4、所有拦截器都返回true。执行目标方法
    5、执行所有拦截器的postHandle方法【倒序】。
    6、前面的步骤有任何异常都会直接触发 afterCompletion【倒序】
    7、页面成功渲染完成以后，会正常触发 afterCompletion【倒序】
*/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("preHandle拦截的请求路径是{}",requestURI);

        //登录检查逻辑
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("user");
        if (loginUser!=null){
            //用户已登录，放行
            return true;
        }
        //用户未登录，拦截，跳转到登录页面
        request.setAttribute("msg","您尚未登录，请登录！");
        request.getRequestDispatcher("/").forward(request,response);
        return false;
    }

    //目标方法执行完成以后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行{}",modelAndView);
    }

    //页面渲染以后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion执行异常{}",ex);
    }
}
