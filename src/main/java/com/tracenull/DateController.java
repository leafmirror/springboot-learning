package com.tracenull;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateController {
    @RequestMapping("/test")
    public String test() {
        return "12";
    }
}
