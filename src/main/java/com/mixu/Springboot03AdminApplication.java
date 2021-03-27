package com.mixu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//使用@ServletComponentScan + 原生的@WebServlet、@WebFilter、@WebListener来实现原生的Web组件注入
//也可以在容器中注册ServletRegistrationBean、FilterRegistrationBean、ServletListenerRegistrationBean
//扫描原生的Servlet，指定要扫描的包；
@ServletComponentScan(basePackages = "com.mixu")
@SpringBootApplication
public class Springboot03AdminApplication {
    /*SpringBoot的定制化：
    定制SpringBoot的常见方式：
        1、编写自定义的配置类 xxxConfiguration + @Bean替换/增加容器中默认组件；
        2、编写application.properties配置文件
        3、编写自定义的编辑器 xxxCustomizer
        4、(重点)Web应用的定制：编写一个配置类实现 WebMvcConfigurer即可定制化web功能 + @Bean给容器中再扩展一些组件
        5、@EnableWebMvc + WebMvcConfigurer + @Bean 可以全面接管SpringMVC，所有规则全部自己重新配置
            实现定制和扩展功能，静态资源、视图解析器、欢迎页等等功能全部失效：
            1.WebMvcAutoConfiguration：默认的SpringMVC的自动配置功能类，里面配了视图解析器、欢迎页等功能
            2.一旦使用@EnableWebMvc，会@Import(DelegatingWebMvcConfiguration.class)
            3.DelegatingWebMvcConfiguration的作用，里面的配置只能保证SpringMVC最基本的使用
                把项目中编写的所有WebMvcConfigurer 拿到，并把里面的全部定制化功能合起来一起生效
                (所以可以编写多个WebMvcConfigurer来定制化功能)
                自动配置了一些非常底层的组件，如RequestMappingHandlerMapping、这些组件依赖的组件都是从容器中获取
            4.WebMvcAutoConfiguration 里面的配置要能生效关乎于一个条件注解
                @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)，容器中没有该组件的时候才生效
                因为使用@EnableWebMvc会给容器中导入一个 DelegatingWebMvcConfiguration
                而DelegatingWebMvcConfiguration 又继承于WebMvcConfigurationSupport
                所以加了@EnableWebMvc后WebMvcAutoConfiguration里面的配置就会失效

    */

    public static void main(String[] args) {
        SpringApplication.run(Springboot03AdminApplication.class, args);
    }

}
