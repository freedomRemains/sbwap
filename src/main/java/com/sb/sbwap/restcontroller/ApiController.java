package com.sb.sbwap.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.sblib.util.ValidationUtil;
import com.sb.sbwap.form.ValidationForm;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

    /** バリデーションユーティリティ */
    private final ValidationUtil validationUtil;

    @PostMapping("/api/v1/validation")
    public ResponseEntity<Map<String, Object>> postValidationByForm(
            @ModelAttribute ValidationForm validationForm,
            BindingResult bindingResult) {
            // @RequestBody ValidationForm validationForm) {

        // バリデーションを実行する
        // BindingResult bindingResult = new BeanPropertyBindingResult(validationForm, "validationForm");
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
}
