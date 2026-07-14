package com.aweqy.picturecloud.controller;

import com.aweqy.picturecloud.model.dto.user.UserRegisterRequest;
import com.aweqy.picturecloud.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private UserService userService;

    @Test
    void userRegister() {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount("testuser");
        request.setUserPassword("12345678");
        request.setCheckPassword("12345678");



    }
}