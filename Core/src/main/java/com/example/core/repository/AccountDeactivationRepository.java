package com.example.core.repository;

import com.example.core.model.entity.AccountDeactivation;
import com.example.core.model.enums.DeactivationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDeactivationRepository extends JpaRepository<AccountDeactivation, Long> {

    @Query("""
            select ad from AccountDeactivation ad
            where ad.id =:userId
            and ad.status = :status
            """)
    Optional<AccountDeactivation> findByUserIdAndStatus(Long userId, DeactivationStatus status);
}
