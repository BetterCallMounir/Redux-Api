package com.example.redux.repository;

import com.example.redux.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken , Long> {
    VerificationToken findByToken(String token);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= :now")
    void deleteExpiredTokens(@Param("now") Instant now);
}
