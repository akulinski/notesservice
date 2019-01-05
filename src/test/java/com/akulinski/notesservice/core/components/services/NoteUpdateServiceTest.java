package com.akulinski.notesservice.core.components.services;

import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.UpdateNoteRequestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteUpdateServiceTest {

    @Autowired
    private MockMvc mockMvc;

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

        UpdateNoteRequestModel noteRequestModel = mock(UpdateNoteRequestModel.class);
        Mockito.when(noteRequestModel.getId()).thenReturn(1);
        Mockito.when(noteRequestModel.getContent()).thenReturn("Mock");

        noteUpdateService.updateNoteContent(noteRequestModel);

        String contentAfterUpdate = notesRepository.findById(2).get().getContent();

        assertNotEquals(currentContent, contentAfterUpdate);

    }
}