package com.example.core.service;

import com.example.core.model.entity.AccountDeactivation;
import com.example.core.model.enums.DeactivationStatus;
import com.example.core.repository.AccountDeactivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDeactivationService {

    private final AccountDeactivationRepository accountDeactivationRepository;

    public Optional<AccountDeactivation> findByUserId(Long userId) {
       return accountDeactivationRepository.findByUserIdAndStatus(userId, DeactivationStatus.ACTIVE);
    }
    public AccountDeactivation save(AccountDeactivation deactivation){
        return accountDeactivationRepository.save(deactivation);
    }
}
