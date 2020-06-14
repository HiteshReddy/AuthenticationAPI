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
public class Credentials implements Serializable {

    private static final long serialVersionUID = -1827481796795199927L;
    private String userName;
    private String password;

}
