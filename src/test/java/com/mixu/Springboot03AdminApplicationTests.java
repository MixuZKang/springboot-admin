package com.mixu;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
class Springboot03AdminApplicationTests {

    //因为导入了的jdbc场景中已经给容器中放了JdbcTemplate，所以可以直接自动装配
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {

        Long aLong = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        log.info("记录总数：{}", aLong);

        log.info("数据源类型：{}",dataSource.getClass());
    }

}
