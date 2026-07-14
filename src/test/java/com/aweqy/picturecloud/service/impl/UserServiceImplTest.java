package com.aweqy.picturecloud.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceImplTest {

    @Test
    void getEncryptPassword() {
        String userPassword = "12345678";
        final String SALT = "aweqy";
        System.out.println(DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes()));

    }
}