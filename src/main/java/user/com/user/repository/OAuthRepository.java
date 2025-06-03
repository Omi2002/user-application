package user.com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import user.com.user.model.OAuth;

public interface OAuthRepository extends JpaRepository<OAuth, Integer> {
    Optional<OAuth> findByRequestIdAndStatus(String requestId, Boolean status); 
}
