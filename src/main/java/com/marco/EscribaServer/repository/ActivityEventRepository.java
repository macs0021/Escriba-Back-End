package com.marco.EscribaServer.repository;

import com.marco.EscribaServer.Utils.EntityType;
import com.marco.EscribaServer.entity.ActivityEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityEventRepository extends JpaRepository<ActivityEvent, Long> {

    @Query("SELECT e FROM ActivityEvent e WHERE e.username IN :usernames ORDER BY e.timestamp DESC")
    List<ActivityEvent> findRecentByUsernames(@Param("usernames") List<String> usernames, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ActivityEvent a WHERE a.entityId = :entityId AND a.entityType = :entityType")
    boolean existsByEntityIdAndEntityType(String entityId, EntityType entityType);

}