package com.mixu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
public class formController {

    @GetMapping("/form_layouts")
    public String form_layouts() {
        return "form/form_layouts";
    }

    /*文件上传原理：
    表单中提交的文件项可以用@RequestPart获取，MultipartFile会自动封装上传的文件
    文件上传自动配置类-MultipartAutoConfiguration-MultipartProperties
    SpringBoot自动配置好了 StandardServletMultipartResolver【文件上传解析器】
        1、请求进来使用文件上传解析器的isMultipart方法判断表单的内容类型是否是multipart
            (所以文件上传表单一定要写enctype="multipart/form-data")
            并使用resolveMultipart方法封装文件上传请求，返回MultipartHttpServletRequest
        2、参数解析器来解析请求中的文件内容，并封装成MultipartFile类型
        3、将request中的文件信息封装为一个Map；MultiValueMap<String, MultipartFile>
        @RequestPart("headerImg")就相当于从这个Map中拿headerImg对应的MultipartFile信息
    底层源码有个工具类FileCopyUtils：实现文件流的拷贝
    */
    @PostMapping("/upload")
    public String upload(@RequestParam("email") String email,
                         @RequestParam("username") String username,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos) throws IOException {
        log.info("上传的信息：email={}，username={}，headerImg={}，photos={}",
                email, username, headerImg.getSize(), photos.length);

        if (!headerImg.isEmpty()) {
            //保存到文件服务器，OSS服务器
            //获取到原始的文件名
            String filename = headerImg.getOriginalFilename();
            headerImg.transferTo(new File("E:\\img\\" + filename));
        }

        if(photos.length > 0){
            for (MultipartFile photo : photos) {
                if(!photo.isEmpty()){
                    String filename = photo.getOriginalFilename();
                    photo.transferTo(new File("E:\\img\\"+filename));
                }
            }
        }
        return "main";
    }
}
