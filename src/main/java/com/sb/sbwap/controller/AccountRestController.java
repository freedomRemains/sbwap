package com.sb.sbwap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.sblib.dto.AccountDto;
import com.sb.sblib.dto.CommonResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@ApiResponses({
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = CommonResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(schema = @Schema(implementation = CommonResponseDto.class)))
})
@RestController
@RequiredArgsConstructor
public class AccountRestController {

    @Operation(description = "アカウント取得API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", 
                    content = @Content(schema = @Schema(implementation = AccountDto.class))),
    })
    @GetMapping("/account")
    public AccountDto getAccount() {
        AccountDto accountDto = new AccountDto();
        return accountDto;
    }
}
