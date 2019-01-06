package com.akulinski.notesservice.core.components.repositories;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface NotesRepository extends CrudRepository<NoteEntity, Integer> {

    Optional<NoteEntity> findByIdAndIsCurrentTrueAndIsDeletedFalse(Integer id);

    ArrayList<NoteEntity> findAllByIsCurrentTrueAndIsDeletedFalse();

    @Override
    ArrayList<NoteEntity> findAll();
}
