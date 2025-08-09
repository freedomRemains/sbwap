package com.sb.sbwap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sb.sbwap.dto.RenderItemsDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RenderController {

    @GetMapping("/render")
    public String getRender(Model model) {

        // モデルに画面表示用のDTOを設定する
        RenderItemsDto renderItemsDto = new RenderItemsDto();
        model.addAttribute("renderItems", renderItemsDto);

        // 遷移先画面名を呼び出し側に返却する
        return "render";
    }
}
