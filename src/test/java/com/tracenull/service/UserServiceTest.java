package com.tracenull.service;

import com.tracenull.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void userList() {
        List<User> users = userService.userList();
        for (User user : users) {
            System.out.println(user.getUserId());
            System.out.println(user.getUserName());
            System.out.println(user.getUserPhone());
        }
    }

    @Test
    void update() {
        userService.update();
        User user = userService.find();
        System.out.println(user.getUserName());
    }
}