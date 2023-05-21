package com.example.core.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionBody {


    private String message;

    private LocalDateTime timestamp;

    private String path;

}