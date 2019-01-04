package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class NotesController {

    private final NotesRepository noteRepository;

    @Autowired
    public NotesController(NotesRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping(value = "getAll")
    public ResponseEntity<ArrayList<NoteEntity>> getAllNotes() {
        return new ResponseEntity<ArrayList<NoteEntity>>(noteRepository.findAll(), HttpStatus.OK);
    }
}
