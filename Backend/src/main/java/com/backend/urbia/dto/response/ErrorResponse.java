package com.backend.urbia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> detalles;

}
