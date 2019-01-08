package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteUpdateServiceTest {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NoteUpdateService noteUpdateService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void updateNoteContent() {

        String currentContent = notesRepository.findById(2).get().getContent();

        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);

        Mockito.when(noteRequestModel.getContent()).thenReturn("Mock");
        when(noteRequestModel.getTitle()).thenReturn("Mock title");

        noteUpdateService.updateNoteContent(noteRequestModel, 2);

        String contentAfterUpdate = notesRepository.findById(2).get().getContent();

        assertNotEquals(currentContent, contentAfterUpdate);

    }

    @Test
    public void countOfCurrentNotesStaysTheSame() {
        Integer currentSize = notesRepository.findAllByIsCurrentTrueAndIsDeletedFalse().size();

        NoteRequestModel noteRequestModel = mock(NoteRequestModel.class);
        when(noteRequestModel.getContent()).thenReturn("Mock note content");
        when(noteRequestModel.getTitle()).thenReturn("Mock title");

        noteUpdateService.updateNoteContent(noteRequestModel, 2);

        Integer sizeAfterUpdate = notesRepository.findAllByIsCurrentTrueAndIsDeletedFalse().size();

        assertEquals(currentSize, sizeAfterUpdate);
    }
}