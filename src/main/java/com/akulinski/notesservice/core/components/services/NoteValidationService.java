package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteValidationService {

    private final NotesRepository notesRepository;

    @Autowired
    public NoteValidationService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public boolean checkIfNoteIsNotDeletedAndIsCurrent(NoteEntity noteEntity) {
        return noteEntity.getIsCurrent() && !noteEntity.getIsDeleted();
    }

    public boolean contentAndTitleNotEmpty(String content, String title) {
        return !StringUtils.isNullOrEmpty(content) && !StringUtils.isNullOrEmpty(title);
    }

    public boolean titleOrContentDifferent(String content, String title, Integer id) {
        NoteEntity noteEntity = notesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Note with id %s not found", id)));

        return !(noteEntity.getContent().equals(content) && noteEntity.getTitle().equals(title));
    }

}
