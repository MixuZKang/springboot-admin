package com.mixu.servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
/*现在程序中有两个Servlet：
    1、MyServlet --> 处理 /my
    2、DispatcherServlet --> 处理 /
  DispatchServlet 如何注册进来：
    容器中自动配置了 DispatcherServlet属性绑定到 WebMvcProperties；对应的配置文件配置项是 spring.mvc
    通过 ServletRegistrationBean<DispatcherServlet>把 DispatcherServlet配置进来
    默认映射的是 / 路径
  原来使用Tomcat做原生的Servlet开发时：
    多个Servlet都能处理到同一层路径，精确优选原则
        DispatcherServlet：/
        MyServlet：/my
    如果发送/ 则是由DispatcherServlet来处理，执行的是Spring的流程
    如果发送/my，因为精确优先所以是由MyServlet处理，执行的是Tomcat相关流程
*/

//使用RegistrationBean，注入原生web组件
@Configuration(proxyBeanMethods = true)//保证依赖的组件始终是单实例的
public class MyRegistrationConfig {

    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet = new MyServlet();
        //传递一个servlet和能处理的请求路径
        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }


    @Bean
    public FilterRegistrationBean myFilter(){

        MyFilter myFilter = new MyFilter();
        //可以传入一个filter和servlet，这个servlet能处理哪些路径它就拦截哪些路径
//        return new FilterRegistrationBean(myFilter,myServlet());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        //设置这个filter能拦截哪些路径
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my","/css/*"));
        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MySwervletContextListener mySwervletContextListener = new MySwervletContextListener();
        return new ServletListenerRegistrationBean(mySwervletContextListener);
    }
}
