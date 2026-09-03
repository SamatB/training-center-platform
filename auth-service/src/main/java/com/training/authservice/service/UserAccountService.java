package com.training.authservice.service;

import com.training.authservice.dto.request.RegisterRequest;
import com.training.authservice.dto.response.UserAccountResponse;
import com.training.authservice.entity.UserAccount;
import com.training.authservice.exception.EntityAlreadyExistsException;
import com.training.authservice.mapper.UserAccountMapper;
import com.training.authservice.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    public UserAccountResponse register(RegisterRequest request) {

        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException(
                    String.format("Пользователь с такой электронной почтой '%s' уже существует.", request.getEmail())
            );
        }

        UserAccount userAccount = userAccountMapper.toEntity(request);

        userAccount.setId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        userAccount.setCreatedAt(now);
        userAccount.setUpdatedAt(now);

        userAccount.setEnabled(true);

        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return userAccountMapper.toResponse(savedUserAccount);
    }
}
