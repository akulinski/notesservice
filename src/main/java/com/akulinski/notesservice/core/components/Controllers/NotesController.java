package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.core.components.services.NoteUpdateService;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.akulinski.notesservice.models.responsemodels.NoteUpdateResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class NotesController {

    private final NotesRepository noteRepository;

    private final NoteUpdateService noteUpdateService;

    @Autowired
    public NotesController(NotesRepository noteRepository, NoteUpdateService noteUpdateService) {
        this.noteRepository = noteRepository;
        this.noteUpdateService = noteUpdateService;
    }

    @GetMapping(value = "get-all")
    public ResponseEntity getAllNotes() {
        return new ResponseEntity<>(noteRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping(value = "update-note")
    public ResponseEntity updateNote(@RequestBody NoteRequestModel noteRequestModel) {

        try {
            noteUpdateService.updateNote(noteRequestModel);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new NoteUpdateResponseModel(String.format("Note with id %s was updated", noteRequestModel.getId())), HttpStatus.OK);
    }

    @GetMapping(value = "get-note-by-id/{noteId}")
    public ResponseEntity getNoteById(@PathVariable String noteId) throws IllegalArgumentException {

        try {
            Integer noteIdIntegerValue = Integer.parseInt(noteId);
            NoteEntity noteEntity = noteRepository.findByIdAndIsCurrentTrueAndIsDeletedFalse(noteIdIntegerValue).get();
            return new ResponseEntity<>(noteEntity, HttpStatus.OK);
        } catch (NumberFormatException | NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
