package com.training.userservice.service;

import com.training.userservice.dto.response.UserResponse;
import com.training.userservice.entity.User;
import com.training.userservice.exception.UserNotFoundException;
import com.training.userservice.mapper.UserMapper;
import com.training.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        log.info("Получение профиля пользователя по id={}", id);

        User user = findUserById(id);

        log.info("Профиль пользователя id={} успешно получен", id);
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info("Получение списка профилей пользователей");

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        log.info("Получено профилей пользователей: {}", users.size());
        return users;
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id={} не найден", id);
                    return new UserNotFoundException(
                            "Пользователь с id=" + id + " не найден"
                    );
                });
    }
}
