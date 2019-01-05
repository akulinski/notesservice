package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.AddNoteRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class NoteCreationService {

    private final NotesRepository notesRepository;

    private final HistoryRepository historyRepository;

    @Autowired
    public NoteCreationService(NotesRepository notesRepository, HistoryRepository historyRepository) {
        this.notesRepository = notesRepository;
        this.historyRepository = historyRepository;
    }

    public NoteEntity createNoteEntityFromRequest(@RequestBody AddNoteRequestModel addNoteRequestModel) {
        NoteEntity noteEntity = new NoteEntity();
        HistoryEntity historyEntity = new HistoryEntity();
        historyRepository.save(historyEntity);
        noteEntity.setHistoryEntity(historyEntity);
        noteEntity.setContent(addNoteRequestModel.getContent());
        notesRepository.save(noteEntity);
        return noteEntity;
    }
}