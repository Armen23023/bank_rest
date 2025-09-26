package com.example.bankcards.util.schedule;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardExpirationSchedulerService {

    private final CardRepository cardRepository;

    /**
     * Runs every day at midnight to update expired cards.
     */
    @Scheduled(cron = "0 0 0 * * ?") // every day at 00:00
    @Transactional
    public void expireCards() {
        var today = LocalDate.now();

        List<Card> expiredCards = cardRepository.findAllByStatusAndExpirationDateBefore(CardStatus.ACTIVE, today);

        for (Card card : expiredCards) {
            card.setStatus(CardStatus.EXPIRED);
        }

        cardRepository.saveAll(expiredCards);
    }
}
