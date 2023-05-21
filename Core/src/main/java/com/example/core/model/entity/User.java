package com.example.core.model.entity;

import com.example.core.model.UserStatus;
import com.example.core.model.request.UserRegistrationRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "core_user")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(name = "keycloak_id",unique = true)
    private String keyCloakId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "picture_url")
    private String pictureUrl;

    public User(UserRegistrationRequest request) {
        this.keyCloakId = request.getId();
        this.firstname = request.getFirstname();
        this.lastname = request.getLastname();
        this.email = request.getEmail();
        this.status = UserStatus.ACTIVE;
    }
}
