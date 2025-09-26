package com.example.bankcards.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
    CARD_NOT_FOUND("Card not found.", HttpStatus.NOT_FOUND),
    CARD_BLOCKED("Card is blocked.",HttpStatus.BAD_REQUEST),
    CARD_EXPIRED("Card is expired.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED("Access denied.", HttpStatus.FORBIDDEN),
    INSUFFICIENT_FUNDS("Insufficient funds.", HttpStatus.BAD_REQUEST),
    CARD_NOT_OWNED_BY_USER("Card not owned by User.", HttpStatus.BAD_REQUEST ),;


    private final String defaultMessage;
    private final HttpStatus status;
}
