package com.akulinski.notesservice.core.components.repositories;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface NotesRepository extends CrudRepository<NoteEntity, Integer> {

    NoteEntity findByIdAndIsDeletedFalse(Integer id);

    @Override
    ArrayList<NoteEntity> findAll();
}
