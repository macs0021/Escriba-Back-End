package com.marco.EscribaServer.repository;

import com.marco.EscribaServer.entity.Reading;
import com.marco.EscribaServer.entity.ReadingID;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ReadingRepository extends CrudRepository<Reading, ReadingID> {
}
