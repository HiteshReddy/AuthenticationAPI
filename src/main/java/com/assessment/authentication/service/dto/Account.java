package com.assessment.authentication.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {

    private String iban;
    private String ownerId;

}
