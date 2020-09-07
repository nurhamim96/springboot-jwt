package com.learn.spring.springbootauthenticationwithjwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorValidation {
    private String field;
    private Object message;
}
