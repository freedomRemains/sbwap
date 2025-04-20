package com.sb.sbwap.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ValidationDto {

    @NotBlank
    private String name;

    @Valid
    @NotEmpty
    private List<ChildDto> childList;
}
