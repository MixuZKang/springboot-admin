package com.mixu.controller;

import com.mixu.exception.UserTooManyException;
import com.mixu.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class tableController {
    /*异常处理：

    默认规则：
        1.默认情况下，SpringBoot提供/error处理所有错误的映射
        2.对于机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。
            对于浏览器客户端，响应一个“whitelabel”错误视图，以HTML格式呈现相同的数据
        3.要对其进行自定义，添加View解析为error。要完全替换默认行为，可以实现ErrorController并注册该类型的Bean定义
            或添加ErrorAttributes类型的组件以使用现有机制但替换其内容。
        4.error/下的404，5xx页面会被自动解析

    异常处理自动配置原理：由 ErrorMvcAutoConfiguration 自动配置异常处理规则
        1.给容器中注册了组件：DefaultErrorAttributes
            它定义了错误页面中可以包含哪些数据，如果觉得错误页面可以取得值不够，则可以自定义该组件
        2.给容器中注册了一个controller：BasicErrorController，它定义了适配响应json或白页的规则
            默认处理/error请求； new ModelAndView("error", model)；页面响应一个error视图
            容器中正好有视图组件 View，id就是error；（响应默认错误页），这个View视图定义为什么样，页面响应的就是什么样
            容器中放了一个BeanNameViewResolver（视图解析器）：按照返回的视图名作为组件的id去容器中找View对象。
            如果不想要返回白页或者想改变默认的错误路径等，则可以自定义该组件
        如果想要返回页面，就会找error视图(StaticView，里面默认定义的就是一个白页)
        3.给容器中注册了一个：DefaultErrorViewResolver
            如果发生错误，会获取到HTTP的状态码(404、500)作为视图页地址（viewName），找到真正的页面
            error/viewName.html。所以如果发生了404错误，则会找到 error/404.html

    异常处理步骤流程(如果发生数学异常)：
        1.执行目标方法，目标方法运行期间有任何异常都会被catch、而且标志当前请求结束；异常用 dispatchException封装
        2.请求结束进入视图解析流程(页面渲染)，调用方法(其中目标方法没有被正常执行所以mv为null，然后传入封装了所发生异常的dispatchException)：
            processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
        3.mv = processHandlerException；处理目标方法handler发生的异常，处理完成返回ModelAndView；
            1、遍历所有的 HandlerExceptionResolver处理器异常解析器，看谁能处理当前异常
                系统默认有的异常解析器：DefaultErrorAttributes(自动配置时放的组件)、HandlerExceptionResolverComposite{
                里面又封装了三个ExceptionResolver：ExceptionHandlerExceptionResolver、ResponseStatusExceptionResolver、DefaultHandlerExceptionResolver}
            2、DefaultErrorAttributes先来处理异常。把异常信息保存到request域，因为这个解析器不能处理该异常，所以返回null
                (如果没人能处理，之后转发到/error来处理的时候就能从request域中取出之前保存的错误信息)
                之后会判断异常解析器解析出来的值是否为null，如果不为null则直接break，如果为null就轮到下一个异常解析器执行
            3、遍历完后发现没有任何解析器能处理数学异常，所以异常被抛出(底层自带的异常解析器默认是不支持异常处理的)
                1.发现没有任何人能处理这个请求所引发的异常，最终底层会发送/error请求，会被底层的BasicErrorController(默认处理/error请求)处理
                2.解析错误视图；遍历所有的 ErrorViewResolver看谁能解析
                3.默认的 DefaultErrorViewResolver，作用是把响应状态码作为错误页的地址，error/500.html
                4.判断模板引擎中有没有error/500.html这个页面，如果有则最终响应这个页面

    定制错误处理逻辑：
        1、自定义错误页：error/404.html、error/5xx.html；有精确的错误状态码页面就匹配精确，没有就找 4xx.html；如果都没有就触发白页
        2、@ControllerAdvice + @ExceptionHandler处理全局异常；底层由 ExceptionHandlerExceptionResolver处理
        3、@ResponseStatus + 自定义异常 ；底层是 ResponseStatusExceptionResolver
            获取到@ResponseStatus注解的信息，底层调用response.sendError(statusCode, resolvedReason)；
            该方法是让tomcat发送/error请求，其中携带了错误状态码，错误信息等
        4、Spring底层的异常，如 参数类型转换异常；由 DefaultHandlerExceptionResolver处理框架底层的异常。
            底层也是调用response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            发送/error请求，如果没有任何人能处理/error请求则交给tomcat处理，响应tomcat原生的错误页面
        5、实现HandlerExceptionResolver，自定义异常解析器。如果把该解析器的优先级设为最高，则就作为默认的全局异常处理规则
        6、ErrorViewResolver 实现自定义处理异常；如果想要改变error/5xx.html这个规则，才会自定义该处理
            但一般都不会自定义这个，因为这是最底层的异常处理，所有的异常最终都能它被捕获：
                只要调用response.sendError()，/error请求就会转给BasicErrorController
                而异常没有任何人能处理，也是tomcat底层调用response.sendError()，把/error请求转给controller
                BasicErrorController要去的页面地址是由 ErrorViewResolver解析的

    */

    // 400(Bad Request)：不带请求参数或者参数类型不对，一般都是浏览器的参数没有传递正确
    @GetMapping("/basic_table")
    public String basic_table(@RequestParam("a") int a){
        int i =10/0;
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model){
        //表格内容的遍历
        List<User> users = Arrays.asList(new User("admin", "123456"),
                new User("zhangsan", "123456"),
                new User("lisi", "123456"),
                new User("wangwu", "123456"));
        model.addAttribute("users",users);

        //手动制造用户数量过多异常
        if(users.size()>4){
            throw new UserTooManyException();
        }
        return "table/dynamic_table";
    }

    @GetMapping("/responsive_table")
    public String responsive_table(){
        return "table/responsive_table";
    }

    @GetMapping("/editable_table")
    public String editable_table(){
        return "table/editable_table";
    }

}
