package com.tracenull.controller;

import com.tracenull.exception.EmptyResutlException;
import com.tracenull.exception.RequestParamException;
import com.tracenull.vo.R;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
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
}
