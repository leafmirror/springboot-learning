package com.tracenull.controller;

import com.tracenull.annotation.ResponseResultBody;
import com.tracenull.exception.ResultException;
import com.tracenull.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * https://mp.weixin.qq.com/s/XtWXyvkxJPwhzTMh9SeU4w
 */
@RestController
@RequestMapping("/helloResult")
@ResponseResultBody
public class HelloResultController {
    private static final HashMap<String, Object> INFO;

    static {
        INFO = new HashMap<String, Object>();
        INFO.put("name", "galaxy");
        INFO.put("age", "70");
    }

    @GetMapping("hello")
    public HashMap<String, Object> hello() {
        return INFO;
    }

    /**
     * 测试重复包裹
     */
    @GetMapping("result")
    public Result<Map<String, Object>> helloResult() {
        return Result.success(INFO);
    }

    @GetMapping("helloError")
    public HashMap<String, Object> helloError() throws Exception {
        throw new Exception("helloError");
    }

    @GetMapping("helloMyError")
    public HashMap<String, Object> helloMyError() throws Exception {
        throw new ResultException();
    }

}
