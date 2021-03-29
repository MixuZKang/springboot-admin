package com.mixu.config;

import com.mixu.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//全面接管SpringMVC
//@EnableWebMvc
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //访问 /static/**的所有请求都去 classpath：/static/中进行匹配
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath：/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义的拦截器
        registry.addInterceptor(new LoginInterceptor())
                //配置拦截哪些路径
                .addPathPatterns("/**")// 配置**的话所有请求都会被拦截，包括静态资源
                //放行哪些路径，在这里放行调所有的静态资源访问路径
                .excludePathPatterns("/", "/login","/css/**","/fonts/**","/images/**","/js/**","/static/**");
    }
}
