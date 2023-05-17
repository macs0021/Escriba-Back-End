package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findOneByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %?1%")
    List<User> findByUsernameContaining(String keyword);

    @Query("SELECT ff FROM User u " +
            "JOIN u.following f " +
            "JOIN f.following ff " +
            "WHERE u.username = ?1 " +
            "AND ff.username != ?1 " +
            "ORDER BY RAND() " +
            "LIMIT 6")
    List<User> findRandomUsersFromFollowers(String username);
}
