package com.example.bankcards.util.specs;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.specs.pojo.CardSearchDetails;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class CardSearchSpecification implements Specification<Card> {

    private final CardSearchDetails search;

    @Override
    public Predicate toPredicate(@NonNull Root<Card> root,CriteriaQuery<?> query,@NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filter by Card Ref
        if (search != null && search.getCardRef() != null) {
            predicates.add(cb.equal(root.get("ref"), search.getCardRef()));
        }

        // Filter by Owner Ref
        if (search != null && search.getOwnerRef() != null) {
            predicates.add(cb.equal(root.get("user").get("ref"), search.getOwnerRef()));
        }

        // Filter by Card Type
        if (search != null && search.getCardType() != null) {
            predicates.add(cb.equal(root.get("type"), search.getCardType()));
        }

        // Filter by Card Status
        if (search != null && search.getCardStatus() != null) {
            predicates.add(cb.equal(root.get("status"), search.getCardStatus()));
        }

        // Filter by Card Number keyword
        if (search != null && StringUtils.isNotBlank(search.getCardNumber())) {
            String keyword = "%" + search.getCardNumber().toLowerCase() + "%";
            Predicate numberMatch = cb.like(cb.lower(root.get("number")), keyword);
            Predicate typeMatch = cb.like(cb.lower(root.get("type")), keyword);
            predicates.add(cb.or(numberMatch, typeMatch));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
