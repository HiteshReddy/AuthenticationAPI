package com.assessment.authentication.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationAppError {

    private String message;
    private int code;

}
