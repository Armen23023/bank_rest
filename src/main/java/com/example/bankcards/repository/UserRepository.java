package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("""
    SELECT u
    FROM User u
    JOIN u.userRoles ur
    WHERE ur.value != 'ADMIN'
""")
    Page<User> findForAdmin(Pageable pageable);

    Optional<User> findByRef(UUID ref);
}
