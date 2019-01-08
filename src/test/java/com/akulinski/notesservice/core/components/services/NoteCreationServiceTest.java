package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteCreationServiceTest {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NoteCreationService noteCreationService;

    @Test
    public void createNoteEntityFromRequest() {
        NoteRequestModel noteRequestModel = getNoteRequestModel();

        NoteEntity noteEntity = noteCreationService.createNoteEntityFromRequest(noteRequestModel);
        assertEquals("Mock note content", noteEntity.getContent());
    }

    private NoteRequestModel getNoteRequestModel() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        when(noteRequestModel.getContent()).thenReturn("Mock note content");
        when(noteRequestModel.getTitle()).thenReturn("Mock title");
        return noteRequestModel;
    }

    @Test
    public void noteIsAddedToRepository() {
        Integer currentSize = notesRepository.findAll().size();

        NoteRequestModel noteRequestModel = getNoteRequestModel();

        NoteEntity noteEntity = noteCreationService.createNoteEntityFromRequest(noteRequestModel);

        assertEquals(currentSize + 1, notesRepository.findAll().size());
    }

    @Test
    public void noteIsCurrentAndNotDeleted() {
        Integer currentSize = notesRepository.findAllByIsCurrentTrueAndIsDeletedFalse().size();

        NoteRequestModel noteRequestModel = getNoteRequestModel();

        noteCreationService.createNoteEntityFromRequest(noteRequestModel);

        assertEquals(currentSize + 1, notesRepository.findAllByIsCurrentTrueAndIsDeletedFalse().size());
    }

}