package com.riyaboutique.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    private boolean success;

    private String message;

    private int statusCode;

    private LocalDateTime timestamp;
}
