package com.assessment.authentication.presentation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails implements Serializable {

    private static final long serialVersionUID = 5162869076350930456L;
    private String accountNumber;
    private String userName;
    private String password;

}
