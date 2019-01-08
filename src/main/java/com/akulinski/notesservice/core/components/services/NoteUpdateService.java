package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.akulinski.notesservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class NoteUpdateService {

    private final NotesRepository notesRepository;


    @Autowired
    public NoteUpdateService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }


    public NoteEntity updateNoteContent(NoteRequestModel noteRequestModel, Integer id) throws IllegalArgumentException {

        NoteEntity currentVersion = notesRepository.findByIdAndIsCurrentTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Note with id %s not found", id)));
        return cloneAndUpdate(currentVersion, noteRequestModel.getContent(), noteRequestModel.getTitle());
    }

    private NoteEntity cloneAndUpdate(NoteEntity noteEntity, String newContent, String title) {
        try {
            clone(noteEntity);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updateNoteEntityWithNewestData(noteEntity, newContent, title);
    }

    private NoteEntity updateNoteEntityWithNewestData(NoteEntity noteEntity, String newContent, String title) {
        noteEntity.setContent(newContent);
        noteEntity.setTitle(title);
        try {
            noteEntity.setDateOfModification(DateUtils.getFormattedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        noteEntity.incrementVersion();
        notesRepository.save(noteEntity);
        return noteEntity;
    }

    private NoteEntity clone(NoteEntity toClone) throws ParseException {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setContent(toClone.getContent());
        noteEntity.setTitle(toClone.getTitle());
        noteEntity.setIsCurrent(false);
        noteEntity.setVersion(toClone.getVersion());
        noteEntity.setHistoryEntity(toClone.getHistoryEntity());
        noteEntity.setDateOfCreation(toClone.getDateOfCreation());
        noteEntity.setDateOfModification(DateUtils.getFormattedDate());
        notesRepository.save(noteEntity);
        return noteEntity;
    }
}
