package com.example.bankcards.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardStatus {
    ACTIVE,
    BLOCKED,
    PENDING,
    EXPIRED
}

