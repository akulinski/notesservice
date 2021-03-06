package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.akulinski.notesservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

@Service
public class NoteCreationService {

    private final NotesRepository notesRepository;

    private final HistoryRepository historyRepository;

    @Autowired
    public NoteCreationService(NotesRepository notesRepository, HistoryRepository historyRepository) {
        this.notesRepository = notesRepository;
        this.historyRepository = historyRepository;
    }

    public NoteEntity createNoteEntityFromRequest(@RequestBody NoteRequestModel noteRequestModel) {
        NoteEntity noteEntity = new NoteEntity();
        HistoryEntity historyEntity = new HistoryEntity();
        historyRepository.save(historyEntity);
        noteEntity.setHistoryEntity(historyEntity);
        noteEntity.setContent(noteRequestModel.getContent());
        noteEntity.setTitle(noteRequestModel.getTitle());
        try {
            noteEntity.setDateOfCreation(DateUtils.getFormattedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notesRepository.save(noteEntity);
        return noteEntity;
    }
}