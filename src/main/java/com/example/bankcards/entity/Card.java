package com.example.bankcards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseEntity{

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "card_status")
    private CardStatus status;

    @Column(name = "balance")
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
