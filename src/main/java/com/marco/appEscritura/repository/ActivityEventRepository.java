package com.marco.appEscritura.repository;

import com.marco.appEscritura.entity.ActivityEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityEventRepository extends JpaRepository<ActivityEvent, Long> {

    @Query("SELECT e FROM ActivityEvent e WHERE e.username IN :usernames ORDER BY e.timestamp DESC")
    List<ActivityEvent> findRecentByUsernames(@Param("usernames") List<String> usernames, Pageable pageable);

}