package com.example.core.model.request;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
}
