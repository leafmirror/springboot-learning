package com.tracenull.controller;

import com.tracenull.domain.User2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://mp.weixin.qq.com/s/lXb4SNOM38Va7vRJYVE-2g
 * Spring 中的 Bean 是线程安全的吗？
 */
@RestController
@Scope(value = "singleton")
public class TestController2 {
    private int var = 0;
    private static int staticVar = 0;

    @Value("${test-int}")
    private int testInt; // 从配置文件中读取变量
    ThreadLocal<Integer> tl = new ThreadLocal<>(); // 用ThreadLocal来封装变量
    @Autowired
    private User2 user; // 注入一个对象来封装变量

    @GetMapping(value = "/test_var")
    public String test() {
        tl.set(1);
        System.out.println("先取一下user对象中的值："+user.getAge()+"===再取一下hashCode:"+user.hashCode());
        user.setAge(1);
        System.out.println("普通变量var:" + (++var) + "===静态变量staticVar:" + (++staticVar) + "===配置变量testInt:" + (++testInt)
                + "===ThreadLocal变量tl:" + tl.get()+"===注入变量user:" + user.getAge());
        return "普通变量var:" + var + ",静态变量staticVar:" + staticVar + ",配置读取变量testInt:" + testInt + ",ThreadLocal变量tl:"
                + tl.get() + "注入变量user:" + user.getAge();
    }
}
