package user.com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import user.com.user.model.User;
//Native Query @Query then type your native query 

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByMobileContaining(String mobile);

    Optional<User> findByMobile(String mobile);

}
