package com.example.CampusConnectService.dto.Auth;

import lombok.Data;

@Data

public class AuthResponseDto {
    public AuthResponseDto(String token){
        this.token=token;
    }
    private String token;
    private String tokenType = "Bearer";
}
