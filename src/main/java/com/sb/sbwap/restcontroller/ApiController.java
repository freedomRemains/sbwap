package com.sb.sbwap.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sb.sblib.util.ValidationUtil;
import com.sb.sbwap.dto.RenderItemsDto;
import com.sb.sbwap.form.ValidationForm;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

    /** バリデーションユーティリティ */
    private final ValidationUtil validationUtil;

    /** テンプレートエンジン */
    private final TemplateEngine templateEngine;

    @PostMapping("/api/v1/validation")
    public ResponseEntity<Map<String, Object>> postValidationByForm(
            @RequestBody ValidationForm validationForm,
            BindingResult bindingResult) {

        // バリデーションを実行する
        var errMsgMap = validationUtil.validate(validationForm, bindingResult);
        if (errMsgMap.isEmpty()) {

            // ステータス(正常)をレスポンスに設定し、呼び出し側に返却する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            return ResponseEntity.ok(response);

        } else {

            // ステータスとエラー情報をレスポンスに設定する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("errors", errMsgMap);

            // レスポンスを返却する
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/v1/render")
    public ResponseEntity<Map<String, Object>> postRender(@RequestBody Map<String, Object> requestBody) {

        // コンテキストを生成し、リクエストを設定する
        Context context = new Context();
        RenderItemsDto renderItems = new RenderItemsDto();
        renderItems.setInputValue((String) requestBody.get("inputValue"));
        renderItems.setRenderError((String) requestBody.get("renderError"));
        context.setVariable("renderItems", renderItems);

        // thymeleafテンプレートエンジンを使ってレンダリングを行う
        String html = templateEngine.process("parts/renderParts", context);
        Map<String, Object> response = new HashMap<>();
        response.put("html", html);

        // レスポンスを返却する
        return ResponseEntity.ok(response);
    }
}
