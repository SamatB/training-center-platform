package com.training.authservice.service;

import com.training.authservice.dto.request.ChangeUserRoleRequest;
import com.training.authservice.dto.request.ChangeUserStatusRequest;
import com.training.authservice.dto.response.DeletedUsersResponse;
import com.training.authservice.dto.response.UserRoleResponse;
import com.training.authservice.dto.response.UserStatusResponse;
import com.training.authservice.dto.response.UserSummaryResponse;
import com.training.authservice.entity.Role;
import com.training.authservice.entity.UserAccount;
import com.training.authservice.exception.EntityNotFoundException;
import com.training.authservice.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("email", "createdAt", "role", "firstName", "lastName");

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public UserRoleResponse changeRole(UUID userId, ChangeUserRoleRequest request) {
        UserAccount target = getById(userId);
        UserAccount currentAdmin = getCurrentUser();

        if (currentAdmin.getId().equals(target.getId()) && request.role() != Role.ADMIN) {
            throw new IllegalArgumentException("Administrator cannot remove their own ADMIN role");
        }

        target.setRole(request.role());
        target.setUpdatedAt(LocalDateTime.now());
        UserAccount saved = userAccountRepository.save(target);
        log.info("User role changed: userId={}, role={}", saved.getId(), saved.getRole());
        return new UserRoleResponse(saved.getId(), saved.getEmail(), saved.getRole());
    }

    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> getUsers(int page, int size, String sortBy, String direction, Role role) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        int safePage = Math.max(page, 0);
        String safeSortBy = ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "createdAt";
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(sortDirection, safeSortBy));

        Page<UserAccount> users = role == null
                ? userAccountRepository.findAll(pageable)
                : userAccountRepository.findAllByRole(role, pageable);

        return users.map(this::toSummary);
    }

    @Transactional(readOnly = true)
    public UserSummaryResponse getUserById(UUID userId) {
        UserAccount user = getById(userId);

        return toSummary(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        UserAccount target = getById(userId);
        UserAccount currentAdmin = getCurrentUser();

        if (currentAdmin.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Администратор не может удалить собственную учётную запись через административный endpoint");
        }

        userAccountRepository.delete(target);
        log.info("Пользователь удалён администратором: userId={}, role={}", target.getId(), target.getRole());
    }

    @Transactional
    public DeletedUsersResponse deleteAllStudents() {
        long count = userAccountRepository.countByRole(Role.STUDENT);
        if (count > 0) {
            userAccountRepository.deleteAllByRole(Role.STUDENT);
        }
        log.info("Массовое удаление студентов завершено: deletedCount={}", count);
        return new DeletedUsersResponse(count);
    }

    @Transactional
    public UserStatusResponse changeStatus(UUID userId, ChangeUserStatusRequest request) {
        UserAccount target = getById(userId);
        UserAccount currentAdmin = getCurrentUser();

        if (currentAdmin.getId().equals(target.getId()) && Boolean.FALSE.equals(request.enabled())) {
            throw new IllegalArgumentException("Administrator cannot disable their own account");
        }

        target.setEnabled(request.enabled());
        target.setUpdatedAt(LocalDateTime.now());
        UserAccount saved = userAccountRepository.save(target);
        log.info("User status changed: userId={}, enabled={}", saved.getId(), saved.getEnabled());
        return new UserStatusResponse(saved.getId(), saved.getEmail(), saved.isEnabled());
    }

    private UserAccount getById(UUID id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' was not found"));
    }

    private UserAccount getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Current user was not found"));
    }

    private UserSummaryResponse toSummary(UserAccount user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}
