package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteUpdateService {

    private final NotesRepository notesRepository;

    private final HistoryRepository historyRepository;

    @Autowired
    public NoteUpdateService(HistoryRepository historyRepository, NotesRepository notesRepository) {
        this.historyRepository = historyRepository;
        this.notesRepository = notesRepository;
    }

    /**
     * @param updateNoteRequestModel
     * @throws IllegalArgumentException
     */
    public void updateNoteContent(NoteRequestModel noteRequestModel, Integer id) throws IllegalArgumentException {

        NoteEntity currentVersion = notesRepository.findByIdAndIsCurrentTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Note with id %s not found", id)));
        cloneAndUpdate(currentVersion, noteRequestModel.getContent(), noteRequestModel.getTitle());
    }

    private void cloneAndUpdate(NoteEntity noteEntity, String newContent, String title) {
        clone(noteEntity);
        updateNoteEntityWithNewestData(noteEntity, newContent, title);
    }

    private void updateNoteEntityWithNewestData(NoteEntity noteEntity, String newContent, String title) {
        noteEntity.setContent(newContent);
        noteEntity.incrementVersion();
        noteEntity.setIsCurrent(true);
        noteEntity.setTitle(title);
        notesRepository.save(noteEntity);
    }

    private void clone(NoteEntity toClone) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setContent(toClone.getContent());
        noteEntity.setTitle(toClone.getTitle());
        noteEntity.setIsCurrent(false);
        noteEntity.setVersion(toClone.getVersion());
        noteEntity.setHistoryEntity(toClone.getHistoryEntity());
        notesRepository.save(noteEntity);
    }

}
