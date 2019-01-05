package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotesControllerTest {

    private final static String EXPECTED_CONTENT_AFTER_UPDATE = "{\"message\":\"Note with id 2 was updated\"}";

    @Value("${notes.mock.count}")
    private String exptectedNumberOfNotes;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotesRepository notesRepository;

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gson = new GsonBuilder().create();
    }

    @Test
    public void getAllNotes() throws Exception {
        this.mockMvc.perform(get("/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(Integer.parseInt(exptectedNumberOfNotes) + 1, noteEntities.size());
                });
    }

    @Test
    public void updateNote() throws Exception {
        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteRequest()))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .string(EXPECTED_CONTENT_AFTER_UPDATE));
    }

    private String createJsonOfNoteRequest() {
        NoteRequestModel noteRequestModel = new NoteRequestModel();
        noteRequestModel.setContent("Mock value");
        noteRequestModel.setId(2);
        return gson.toJson(noteRequestModel);
    }

    @Test
    public void getNoteById() throws Exception {

        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteRequest()));

        this.mockMvc.perform(get("/get-note-by-id/2"))
                .andExpect(status()
                        .isOk())
                .andExpect(content().string(containsString("Mock value")));
    }

    @Test
    public void getNoteByIdThatIsOutOfRange() throws Exception {
        this.mockMvc.perform(get("/get-note-by-id/10000000000000000"))
                .andExpect(status()
                        .isBadRequest());
    }


    @Test
    public void getNoteByIdThatDosntExists() throws Exception {
        this.mockMvc.perform(get("/get-note-by-id/1000"))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content().string("No value present"));
    }

}