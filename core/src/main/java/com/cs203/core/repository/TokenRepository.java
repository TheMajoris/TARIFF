package com.cs203.core.repository;

import com.cs203.core.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenAndExpiresAtAfter(UUID token, OffsetDateTime expiresAt);
}
