package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteValidationServiceTest {

    @Autowired
    private NoteValidationService noteValidationService;


    @Test
    public void checkIfNoteIsNotDeletedAndIsCurrent() {
        NoteEntity noteEntity = getNoteEntity(false);
        assertTrue(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }

    private NoteEntity getNoteEntity(boolean b) {
        NoteEntity noteEntity = mock(NoteEntity.class);
        Mockito.when(noteEntity.getIsCurrent()).thenReturn(true);
        Mockito.when(noteEntity.getIsDeleted()).thenReturn(b);
        return noteEntity;
    }


    @Test
    public void checkIsNotCurrent() {
        NoteEntity noteEntity = mock(NoteEntity.class);
        Mockito.when(noteEntity.getIsCurrent()).thenReturn(false);
        assertFalse(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }


    @Test
    public void checkIsDeleted() {
        NoteEntity noteEntity = getNoteEntity(true);
        assertFalse(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }

    @Test
    public void contentNotEmpty() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        Mockito.when(noteRequestModel.getContent()).thenReturn("Not empty");
        when(noteRequestModel.getTitle()).thenReturn("Mock title");

        assertTrue(noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle()));
    }

    @Test
    public void titleEmpty() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        Mockito.when(noteRequestModel.getContent()).thenReturn("Not empty");
        when(noteRequestModel.getTitle()).thenReturn("");

        assertFalse(noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle()));
    }

    @Test
    public void titleNull() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        Mockito.when(noteRequestModel.getContent()).thenReturn("Not empty");
        when(noteRequestModel.getTitle()).thenReturn(null);

        assertFalse(noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle()));
    }

    @Test
    public void contentEmpty() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        Mockito.when(noteRequestModel.getContent()).thenReturn("");
        when(noteRequestModel.getTitle()).thenReturn("Mock title");

        assertFalse(noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle()));
    }

    @Test
    public void contentNull() {
        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        Mockito.when(noteRequestModel.getContent()).thenReturn(null);
        when(noteRequestModel.getTitle()).thenReturn("Mock title");
        assertFalse(noteValidationService.contentAndTitleNotEmpty(noteRequestModel.getContent(), noteRequestModel.getTitle()));
    }
}