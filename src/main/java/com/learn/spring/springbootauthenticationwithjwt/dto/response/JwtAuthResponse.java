package com.learn.spring.springbootauthenticationwithjwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthResponse {
    private String token;
}
