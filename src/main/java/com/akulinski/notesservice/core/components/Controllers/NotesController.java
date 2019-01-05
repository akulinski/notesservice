package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.core.components.services.NoteCreationService;
import com.akulinski.notesservice.core.components.services.NoteUpdateService;
import com.akulinski.notesservice.core.components.services.NoteValidationService;
import com.akulinski.notesservice.models.requestmodels.AddNoteRequestModel;
import com.akulinski.notesservice.models.requestmodels.UpdateNoteRequestModel;
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

    private final HistoryRepository historyRepository;

    private final NoteCreationService noteCreationService;

    private final NoteValidationService noteValidationService;

    @Autowired
    public NotesController(NotesRepository noteRepository, NoteUpdateService noteUpdateService, HistoryRepository historyRepository, NoteCreationService noteCreationService, NoteValidationService noteValidationService) {
        this.noteRepository = noteRepository;
        this.noteUpdateService = noteUpdateService;
        this.historyRepository = historyRepository;
        this.noteCreationService = noteCreationService;
        this.noteValidationService = noteValidationService;
    }

    @GetMapping(value = "get-all")
    public ResponseEntity<java.util.ArrayList<NoteEntity>> getAllNotes() {
        return new ResponseEntity<>(noteRepository.findAllByIsCurrentTrueAndIsDeletedFalse(), HttpStatus.OK);
    }

    @PutMapping(value = "update-note")
    public ResponseEntity updateNote(@RequestBody UpdateNoteRequestModel updateNoteRequestModel) {

        if (noteValidationService.contentNotEmpty(updateNoteRequestModel.getContent())) {
            try {
                noteUpdateService.updateNoteContent(updateNoteRequestModel);
            }catch (IllegalArgumentException illegalArgumentException){
                return new ResponseEntity<>(String.format("Note with given id(%s) does not exist ", updateNoteRequestModel.getId()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Cannot assign empty content to note", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new NoteUpdateResponseModel(String.format("Note with id %s was updated", updateNoteRequestModel.getId())), HttpStatus.OK);
    }

    @GetMapping(value = "get-note-by-id/{noteId}")
    public ResponseEntity getNoteById(@PathVariable String noteId) throws IllegalArgumentException {

        try {

            Integer noteIdIntegerValue = Integer.parseInt(noteId);
            NoteEntity noteEntity = noteRepository.findByIdAndIsCurrentTrueAndIsDeletedFalse(noteIdIntegerValue).get();

            if (noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity)) {
                return new ResponseEntity<>(noteEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format("Note with given id(%s) does not exist ", noteId), HttpStatus.BAD_REQUEST);
            }

        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>(String.format("Note with given id(%s) does not exist ", noteId), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "add-note")
    public ResponseEntity addNote(@RequestBody AddNoteRequestModel addNoteRequestModel) {

        if (noteValidationService.contentNotEmpty(addNoteRequestModel.getContent())) {
            NoteEntity noteEntity = noteCreationService.createNoteEntityFromRequest(addNoteRequestModel);
            return new ResponseEntity<>(noteEntity, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Note content cannot be empty", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "delete-note/{noteId}")
    public ResponseEntity<String> deleteNoteWithId(@PathVariable String noteId) {

        try {
            NoteEntity noteEntity = noteRepository.findById(Integer.parseInt(noteId)).get();

            if (noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity)) {
                noteEntity.setIsDeleted(true);
                noteRepository.save(noteEntity);
                return new ResponseEntity<>(String.format("Note with id %s was removed", noteId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format("Note with given id(%s) does not exist ", noteId), HttpStatus.BAD_REQUEST);
            }

        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>(String.format("Note with given id(%s) does not exist ", noteId), HttpStatus.BAD_REQUEST);
        }

    }

}
