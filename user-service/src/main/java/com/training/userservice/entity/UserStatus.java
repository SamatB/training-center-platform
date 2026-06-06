package com.training.userservice.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum UserStatus {
    ACTIVE,
    BLOCKED,
    DELETED
}
