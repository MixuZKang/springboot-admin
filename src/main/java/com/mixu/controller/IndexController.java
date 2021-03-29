package com.mixu.controller;


import com.mixu.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class IndexController {


    /*视图解析原理：
    目标方法处理的过程中，所有数据都会被放在 ModelAndViewContainer 里面，其中也包括数据和视图地址
    如果方法的参数是一个自定义类型对象（里面的值是从请求参数中确定的），也把他放在 ModelAndViewContainer

    1、目标方法处理结束后会拿到返回值，然后判断使用哪个返回值处理器来处理，最终找到处理字符串的处理器
    2、把该String返回值放到 ModelAndViewContainer 里面
    3、任何目标方法执行完成后都会返回 ModelAndView（里面包含了数据和视图地址，原本是放到ModelAndViewContainer里面，
    是从里面抽取出来的），到此视图还没跳转
    4、来到processDispatchResult：处理派发结果（页面改如何响应，跳转），视图解析原理就在该方法中
       该方法调用render(mv, request, response)：进行页面渲染逻辑。mv就是ModelAndView
         根据方法的String返回值得到View视图对象，它定义了页面的渲染逻辑
           1.遍历所有的 ViewResolvers视图解析器 尝试哪个解析器能根据当前返回值得到View对象
           2.拿到内容协商的视图解析器ContentNegotiationViewResolver，里面包含了其他所有的视图解析器
            所以它内部其实是利用其他的视图解析器来得到视图对象的
           3.内部使用Thymeleaf的视图解析器，根据当前返回值redirect:/main.html，判断该String返回值是否是以“redirect:”开头的
            如果是则确定该方法是重定向携带数据的，然后Thymeleaf会帮我们new一个RedirectView
           4.View是一个接口，里面声明了一个render()方法，不同的View对象通过实现它来定制自己的响应页面的策略
           得到RedirectView视图对象后，该对象就会调用他自己的render()方法，来决定如何响应页面，进行页面渲染

    RedirectView(最终的渲染效果就是重定向到一个页面)如何渲染的：
        1、获取目标url地址
        2、调用原生的重定向方法：response.sendRedirect(encodedURL);

    如果返回值以“forward:”开始： new一个InternalResourceView(forwardUrl);
    里面调用的就是原生的转发方法：request.getRequestDispatcher(path).forward(request, response);

    总结：视图解析器会根据返回值返回的不同规则，得到对应的解析器。解析器会返回对应的View视图对象
    不同视图对象中定义了不同的渲染页面的功能。我们也能自定义视图解析器+自定义视图来完成更复杂的功能

    */

    @GetMapping(value = {"/", "/login"})
    public String loginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model) {
        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassWord())) {
            //登录成功往session放user信息
            session.setAttribute("user",user);
            //如果直接在该方法跳转到main页面则会导致去到main页面时携带的是/login请求，当刷新时就会导致表单重复提交
            //登陆成功重定向到main页面，防止表单重复提交
            return "redirect:/toMain";
        }else{
            //登录失败回到登录页面，并给出提示信息
            model.addAttribute("msg","账号或密码错误！");
            return "login";
        }

    }

    //处理toMain请求去到main页面
    @GetMapping("/toMain")
    public String mainPage(HttpSession session,Model model) {
        log.info("当前方法是：{}","mainPage");

        //在来到main页面之前判断是否登录  优选拦截器、过滤器实现
        /*Object loginUser = session.getAttribute("user");
        if(loginUser!=null){
            return "main";
        }else {
            model.addAttribute("msg","您尚未登录，请登录！");
            return "login";
        }*/

        return "main";
    }

}
