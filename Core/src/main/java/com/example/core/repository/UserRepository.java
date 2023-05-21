package com.example.core.repository;

import com.example.core.model.UserStatus;
import com.example.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            select u from User u 
            where u.id = :id
            and u.status in (:statuses)
            """)
    Optional<User> findByIdAndStatus(@Param("id") Long id, @Param("statuses") List<UserStatus> statuses);

    @Query("""
            select u from User u 
            where u.keyCloakId = :id
            and u.status in (:statuses)
            """)
    Optional<User> findByKeycloakIdAndStatus(@Param("id") String keyCloakId, @Param("statuses") List<UserStatus> statuses);
}
