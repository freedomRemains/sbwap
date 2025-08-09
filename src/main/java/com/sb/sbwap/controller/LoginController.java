package com.sb.sbwap.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/top")
    public String getTop(HttpSession session) {

        // redisセッションに情報を書き込む
        session.setAttribute("redisSessionKey", "This is redis session test.");

        return "top";
    }
}
