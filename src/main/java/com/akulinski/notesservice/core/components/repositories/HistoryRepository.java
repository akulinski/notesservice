package com.akulinski.notesservice.core.components.repositories;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface HistoryRepository extends CrudRepository<HistoryEntity, Integer> {

    @Override
    ArrayList<HistoryEntity> findAll();
}
