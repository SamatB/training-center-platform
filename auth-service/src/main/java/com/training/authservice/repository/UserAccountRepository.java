package com.training.authservice.repository;

import com.training.authservice.entity.Role;
import com.training.authservice.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<UserAccount> findByEmailAndEnabledTrue(String email);

    Page<UserAccount> findAllByRole(Role role, Pageable pageable);

    long countByRole(Role role);

    long deleteAllByRole(Role role);
}
