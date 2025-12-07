package com.sb.sbwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.sblib.util.MailUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailUtil mailUtil;

    @GetMapping("mail")
    public String getMail() {
        return "mail";
    }

    @GetMapping("mail/send")
    public String getMailSend(Model model) {

        // メールを送信する
        mailUtil.createMail()
                .from("from.notexist.com")
                .to("to@notexist.com")
                .cc("cc@notexist.com")
                .send("MailUtilテスト", "MailUtilテストの本文です。");

        // 画面に表示するメッセージを作成し、モデルに設定する
        model.addAttribute("message", "メールを送信しました。MailHogを確認してください。");

        // 遷移先ページを呼び出し側に返却する
        return "mail";
    }
}
