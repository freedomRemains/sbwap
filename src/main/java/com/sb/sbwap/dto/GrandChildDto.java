package com.sb.sbwap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GrandChildDto {

    @NotBlank
    private String grandChildName;
}
