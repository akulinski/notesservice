package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("history")
public class HistoryController {

    private final HistoryRepository historyRepository;

    private final NotesRepository notesRepository;

    @Autowired
    public HistoryController(HistoryRepository historyRepository, NotesRepository notesRepository) {
        this.historyRepository = historyRepository;
        this.notesRepository = notesRepository;
    }

    @GetMapping(value = "get-full-history-of-all-notes")
    public ResponseEntity<java.util.ArrayList<HistoryEntity>> getFullHistoryOfAllNotes() {
        return new ResponseEntity<>(historyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "get-history/{id}")
    public ResponseEntity getHistory(@PathVariable String id) {
        try {
            HistoryEntity historyEntity = getHistoryEntity(id);
            return new ResponseEntity(historyEntity.getNoteEntitySet(), HttpStatus.OK);
        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>("Id value of of range ", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException illegalArgumentException) {
            return getResponseEntityWhenNoteDoesNotExist(id, "Note with given id(%s) does not exist ", HttpStatus.BAD_REQUEST);
        }
    }

    private HistoryEntity getHistoryEntity(@PathVariable String id) throws IllegalStateException {
        return notesRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new IllegalArgumentException(String.format("Note with id %s not found", id))).getHistoryEntity();
    }

    private ResponseEntity<String> getResponseEntityWhenNoteDoesNotExist(@PathVariable String id, String s, HttpStatus badRequest) {
        return new ResponseEntity<>(String.format(s, id), badRequest);
    }
}
