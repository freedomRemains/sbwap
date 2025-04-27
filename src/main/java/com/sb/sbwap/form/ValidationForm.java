package com.sb.sbwap.form;

import java.util.List;

import com.sb.sbwap.dto.ChildDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ValidationForm {

    @NotBlank
    private String name;

    @Valid
    @NotEmpty
    private List<ChildDto> childList;
}
