package com.mixu.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@Configuration
public class MyDataSourceConfig {
    /*引入数据源：
    以前引入数据源需要在xml中给容器注册一个bean并使用<property>给数据源的属性设置值
    现在只需要在配置类中@Bean注册数据源即可

    SpringBoot默认的自动配置@ConditionalOnMissingBean(DataSource.class)：
        判断容器中没有DataSource才会自己配一个数据源，如果有则用自己的
    */
    //DruidDataSource数据源里的属性跟配置文件中的spring.datasource配置项进行绑定
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        //手动设置数据源里的属性
//        druidDataSource.setUrl();
//        druidDataSource.setUsername();
//        druidDataSource.setPassword();
        //Filters中添加stat值：加入监控功能   添加wall值：开启防火墙功能
//        druidDataSource.setFilters("stat,wall");
        //设置数据源最大活跃的线程数量
//        druidDataSource.setMaxActive(10);
        return druidDataSource;
    }

    //配置Druid的监控页功能
    @Bean
    public ServletRegistrationBean statViewServlet() {
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet, "/druid/*");
        //添加访问监控页的登录功能
        registrationBean.addInitParameter("loginUsername", "admin");
        registrationBean.addInitParameter("loginPassword", "123456");

        return registrationBean;
    }

    //WebStatFilter：用于采集web-jdbc关联监控的数据，监控web应用
    @Bean
    public FilterRegistrationBean webStatFilter(){
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(webStatFilter);
        //配置拦截路径
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        //排除掉哪些路径不拦截
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }

}
