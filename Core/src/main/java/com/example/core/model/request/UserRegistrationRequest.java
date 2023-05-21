package com.example.core.model.request;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String id;
    private String email;
    private String firstname;
    private String lastname;

}
