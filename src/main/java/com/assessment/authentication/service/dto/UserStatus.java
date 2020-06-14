package com.assessment.authentication.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

    private LocalDateTime lastInvalidAccess;
    private int invalidAttempts;
    private Status status;

    public enum Status {
        BLOCKED, NOT_BLOCKED
    }
}
