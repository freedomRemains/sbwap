package com.sb.sbwap.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.sblib.service.OutputLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final OutputLogService outputLogService;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/top")
    public String getTop(HttpSession session) {

        // redisセッションに情報を書き込む
        session.setAttribute("redisSessionKey", "This is redis session test.");

        // このログは「SBWAP_LOG」設定により、「sbwap.log」に出力される
        log.trace("This is a trace log message.");
        log.info("This is an info log message.");
        log.warn("This is a warn log message.");
        log.error("This is an error log message.");

        // このログは「com.sb.sblib」パッケージ配下なので、「sbwap.log」に出力されない
        outputLogService.outputSampleLogs();

        return "top";
    }
}
