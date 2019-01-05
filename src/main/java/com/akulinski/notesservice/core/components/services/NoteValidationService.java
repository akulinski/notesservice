package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class NoteValidationService {

    public boolean checkIfNoteIsNotDeletedAndIsCurrent(NoteEntity noteEntity) {
        return noteEntity.getIsCurrent() && !noteEntity.getIsDeleted();
    }

    public boolean contentNotEmpty(String content) {
        return !StringUtils.isNullOrEmpty(content);
    }

}
