package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.core.components.services.NoteCreationService;
import com.akulinski.notesservice.core.components.services.NoteUpdateService;
import com.akulinski.notesservice.core.components.services.NoteValidationService;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.akulinski.notesservice.models.responsemodels.NoteUpdateResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("notes")
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

    @PutMapping(value = "update-note/{id}")
    public ResponseEntity updateNote(@RequestBody NoteRequestModel noteRequestModel, @PathVariable String id) {

        boolean isValidRequest = noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle());

        if (isValidRequest) {
            ResponseEntity x = tryToUpdateAndReturnResponse(noteRequestModel, id);
            return x;
        } else {
            return new ResponseEntity<>("Cannot assign empty content to note", HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity tryToUpdateAndReturnResponse(@RequestBody NoteRequestModel noteRequestModel, @PathVariable String id) {
        try {
            if (noteValidationService.titleOrContentDifferent(noteRequestModel.getContent(), noteRequestModel.getTitle(), Integer.parseInt(id))) {
                noteUpdateService.updateNoteContent(noteRequestModel, Integer.parseInt(id));
                return new ResponseEntity<>(new NoteUpdateResponseModel(String.format("Note with id %s was updated", id)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new NoteUpdateResponseModel(String.format("No changes found")), HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException illegalArgumentException) {
            return getResponseEntityWhenNoteDoesNotExist(id, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity getResponseEntityWhenNoteDoesNotExist(@PathVariable String id, String s, HttpStatus badRequest) {
        return new ResponseEntity<>(String.format(s, id), badRequest);
    }

    @GetMapping(value = "get-note-by-id/{noteId}")
    public ResponseEntity getNoteById(@PathVariable String noteId) throws IllegalArgumentException {

        try {

            Integer noteIdIntegerValue = Integer.parseInt(noteId);
            NoteEntity noteEntity = noteRepository.findByIdAndIsCurrentTrueAndIsDeletedFalse(noteIdIntegerValue).get();

            if (noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity)) {
                return new ResponseEntity<>(noteEntity, HttpStatus.OK);
            } else {
                return getResponseEntityWhenNoteDoesNotExist(noteId, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
            }

        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return getResponseEntityWhenNoteDoesNotExist(noteId, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "add-note")
    public ResponseEntity addNote(@RequestBody NoteRequestModel noteRequestModel) {

        if (noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle())) {
            NoteEntity noteEntity = noteCreationService.createNoteEntityFromRequest(noteRequestModel);
            return new ResponseEntity<>(noteEntity, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Note content cannot be empty", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "delete-note/{noteId}")
    public ResponseEntity deleteNoteWithId(@PathVariable String noteId) {

        try {
            NoteEntity noteEntity = noteRepository.findById(Integer.parseInt(noteId)).get();

            return tryToDeleteNoteAndReturnResponse(noteId, noteEntity);

        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return getResponseEntityWhenNoteDoesNotExist(noteId, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity tryToDeleteNoteAndReturnResponse(@PathVariable String noteId, NoteEntity noteEntity) {
        if (noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity)) {
            noteEntity.setIsDeleted(true);
            noteRepository.save(noteEntity);
            return getResponseEntityWhenNoteDoesNotExist(noteId, "Note with id %s was removed", HttpStatus.OK);
        } else {
            return getResponseEntityWhenNoteDoesNotExist(noteId, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
        }
    }

}
