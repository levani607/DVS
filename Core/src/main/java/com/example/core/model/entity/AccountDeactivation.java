package com.example.core.model.entity;

import com.example.core.model.enums.DeactivationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountDeactivation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_taken")
    @CreationTimestamp
    private LocalDate actionTaken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeactivationStatus status;

    public AccountDeactivation(User user) {
        this.user = user;
        this.status = DeactivationStatus.ACTIVE;
    }
}
