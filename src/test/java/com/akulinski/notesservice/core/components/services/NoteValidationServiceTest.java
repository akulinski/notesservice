package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.models.requestmodels.AddNoteRequestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class NoteValidationServiceTest {

    private NoteValidationService noteValidationService;

    @Before
    public void setUp() throws Exception {
        noteValidationService = new NoteValidationService();
    }

    @Test
    public void checkIfNoteIsNotDeletedAndIsCurrent() {
        NoteEntity noteEntity = mock(NoteEntity.class);
        Mockito.when(noteEntity.getIsCurrent()).thenReturn(true);
        Mockito.when(noteEntity.getIsDeleted()).thenReturn(false);
        assertTrue(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }


    @Test
    public void checkIsNotCurrent() {
        NoteEntity noteEntity = mock(NoteEntity.class);
        Mockito.when(noteEntity.getIsCurrent()).thenReturn(false);
        assertFalse(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }


    @Test
    public void checkIsDeleted() {
        NoteEntity noteEntity = mock(NoteEntity.class);
        Mockito.when(noteEntity.getIsCurrent()).thenReturn(true);
        Mockito.when(noteEntity.getIsDeleted()).thenReturn(true);
        assertFalse(noteValidationService.checkIfNoteIsNotDeletedAndIsCurrent(noteEntity));
    }

    @Test
    public void contentNotEmpty() {
        AddNoteRequestModel addNoteRequestModel = mock(AddNoteRequestModel.class);
        Mockito.when(addNoteRequestModel.getContent()).thenReturn("Not empty");
        assertTrue(noteValidationService.contentNotEmpty(addNoteRequestModel.getContent()));
    }

    @Test
    public void contentEmpty() {
        AddNoteRequestModel addNoteRequestModel = mock(AddNoteRequestModel.class);
        Mockito.when(addNoteRequestModel.getContent()).thenReturn("");
        assertFalse(noteValidationService.contentNotEmpty(addNoteRequestModel.getContent()));
    }

    @Test
    public void contentNull() {
        AddNoteRequestModel addNoteRequestModel = mock(AddNoteRequestModel.class);
        Mockito.when(addNoteRequestModel.getContent()).thenReturn(null);
        assertFalse(noteValidationService.contentNotEmpty(addNoteRequestModel.getContent()));
    }
}