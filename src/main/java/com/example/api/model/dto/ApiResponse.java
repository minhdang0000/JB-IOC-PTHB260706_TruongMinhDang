package com.example.api.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse<T>{
    private boolean success;
    private String message;
    private T data;
    private String errors;
    private int httpStatus;
}
