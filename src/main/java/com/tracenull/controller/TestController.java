package com.tracenull.controller;

import com.tracenull.exception.EmptyResutlException;
import com.tracenull.exception.RequestParamException;
import com.tracenull.redislock.RedisLock;
import com.tracenull.redislock.RedisLockUtils;
import com.tracenull.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TestController {
    @Autowired
    private RedisLockUtils utils;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("getString")
    public R getString(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new RequestParamException(1002, "请求参数name为空");
        } else if ("java学习".equals(name)) {
            // 这里没有查询操作，当请求参数是Java旅途的时候，模拟成查询结果为空
            throw new EmptyResutlException(1001, "查询结果为空");
        }

        // 这里模拟一下除自定义异常外的其他两种异常
        int i = 0;
        i = 5 / i;
        return new R().fillData(name);
    }

    @RequestMapping(value = "test/{key}", method = RequestMethod.GET)
    public String test(@PathVariable("key") String key) {
        RedisLock lock = utils.getLock(key, 10000L, 5000L);
        return (lock == null) ? "获取失败" : "获取成功";
    }

    @RequestMapping(value = "nuTest", method = RequestMethod.GET)
    public String nuTest() {
        redisTemplate.delete("redisLock");
        redisTemplate.delete("redisLock2");
        redisTemplate.delete("redisLock3");
        return "成功";
    }

    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(String key) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(uuid);
    }
}