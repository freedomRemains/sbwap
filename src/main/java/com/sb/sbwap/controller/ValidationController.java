package com.sb.sbwap.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sb.sbwap.dto.ChildDto;
import com.sb.sbwap.dto.GrandChildDto;
import com.sb.sbwap.form.ValidationForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ValidationController {

    /** スマートバリデータ */
    private final SmartValidator validator;

    @GetMapping("/validation")
	public String getValidation(Model model) {

        // 画面表示のため、モデルにDTOを設定する
        ValidationForm validationForm = new ValidationForm();
        validationForm.setChildList(new ArrayList<ChildDto>());
        validationForm.getChildList().add(new ChildDto());
        validationForm.getChildList().add(new ChildDto());
        validationForm.getChildList().stream().forEach(child -> {
            child.setGrandChildList(new ArrayList<GrandChildDto>());
            child.getGrandChildList().add(new GrandChildDto());
            child.getGrandChildList().add(new GrandChildDto());
        });
        model.addAttribute("validationForm", validationForm);

        // 遷移先画面名を呼び出し側に返却する
        return "validation";
	}

    @PostMapping("/validation")
    public String postValidation(@ModelAttribute ValidationForm validationForm,
            BindingResult bindingResult,
            Model model) {

        // バリデーションエラーがある場合、エラーメッセージを表示するために、再度画面を表示する
        validator.validate(validationForm, bindingResult);
        if (bindingResult.hasErrors()) {

            // エラーがある場合はエラーメッセージを表示するため、再度同じ画面を表示する
            return "validation";
        }

        // 遷移先画面名を呼び出し側に返却する
		return "top";
	}
}
