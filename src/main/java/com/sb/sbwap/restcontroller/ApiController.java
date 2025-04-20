package com.sb.sbwap.restcontroller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.sb.sbwap.dto.ChildDto;
import com.sb.sbwap.dto.GrandChildDto;
import com.sb.sbwap.dto.ValidationDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final SmartValidator validator;
    private final SpringTemplateEngine templateEngine;

    @GetMapping("/api/v1/validation")
	public Map<String, String> getValidation() {

        // 画面表示のためのDTOを作成する
        ValidationDto validationDto = new ValidationDto();
        validationDto.setChildList(new ArrayList<ChildDto>());
        validationDto.getChildList().add(new ChildDto());
        validationDto.getChildList().add(new ChildDto());
        validationDto.getChildList().stream().forEach(child -> {
            child.setGrandChildList(new ArrayList<GrandChildDto>());
            child.getGrandChildList().add(new GrandChildDto());
            child.getGrandChildList().add(new GrandChildDto());
        });

        // thymeleafでレンダリングした結果をJSONとして返却する
        Context context = new Context();
        context.setVariable("validationDto", validationDto);
        String html = templateEngine.process("parts/validationArea :: validationArea", context);
        Map<String, String> response = Map.of("html", html);
        return response;
    }

    @PostMapping("/api/v1/validation")
	public Map<String, String> postValidation(@RequestBody ValidationDto validationDto,
            BindingResult bindingResult) {

        // バリデーションを実行する
        validator.validate(validationDto, bindingResult);

        // バリデーション結果も含め、thymeleafのテンプレートエンジンでHTMLをレンダリングする
        Context context = new Context();
        context.setVariable("validationDto", validationDto);
        String html = templateEngine.process("parts/validationArea :: validationArea", context);
        Map<String, String> response = Map.of("html", html);

        // バリデーションエラーがある場合、エラーメッセージを表示するために、再度画面を表示する
        if (bindingResult.hasErrors()) {

            // エラーがある場合はエラーメッセージを表示するため、再度同じ画面を表示する
            return response;
        }

        // 遷移先画面名を呼び出し側に返却する
		return response;
	}
}
