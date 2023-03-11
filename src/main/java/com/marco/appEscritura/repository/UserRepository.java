package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findOneByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
