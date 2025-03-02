package com.example.fakestore.repository;

import com.example.fakestore.entity.RefreshToken;
import com.example.fakestore.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Query("SELECT r FROM RefreshToken r WHERE r.tokenId = :tokenId")
    Optional<RefreshToken> findByTokenId(String tokenId);

    @Query("DELETE FROM RefreshToken r WHERE r.tokenId = :tokenId")
    @Modifying
    @Transactional
    void deleteByTokenId(String tokenId);

    @Query("SELECT r FROM RefreshToken r WHERE r.user = :user")
    Optional<RefreshToken> findByUser(User user);
}
