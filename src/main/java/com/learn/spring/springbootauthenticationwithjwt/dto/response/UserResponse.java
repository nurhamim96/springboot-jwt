package com.learn.spring.springbootauthenticationwithjwt.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
}
