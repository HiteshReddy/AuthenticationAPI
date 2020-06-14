package com.assessment.authentication.presentation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String jwtToken;

    public static TokenResponse of(String token) {
        return new TokenResponse((token));
    }

}
