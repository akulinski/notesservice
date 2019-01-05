package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.models.requestmodels.AddNoteRequestModel;
import com.akulinski.notesservice.models.requestmodels.UpdateNoteRequestModel;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gson = new GsonBuilder().create();
    }


    @Test
    public void updateNote() throws Exception {
        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", 2)))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .string(EXPECTED_CONTENT_AFTER_UPDATE));
    }

    @Test
    public void updateNoteWithEmptyContent() throws Exception {
        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("", 2)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .string("Cannot assign empty content to note"));
    }

    @Test
    public void updateNoteWithIdOutOfRange() throws Exception {
        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", 2000000000)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .string("Note with given id(200000000) does not exist "));
    }

    @Test
    public void updateNoteThatDoesntExist() throws Exception {
        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", 100)))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .string("Note with given id(100) does not exist "));
    }

    private String createJsonOfNoteUpdateRequest(String content, int id) {
        UpdateNoteRequestModel updateNoteRequestModel = new UpdateNoteRequestModel();
        updateNoteRequestModel.setContent(content);
        updateNoteRequestModel.setId(id);
        return gson.toJson(updateNoteRequestModel);
    }

    private String createJsonOfNoteAddRequest() {

        AddNoteRequestModel updateNoteRequestModel = new AddNoteRequestModel();

        updateNoteRequestModel.setContent("Mock value");
        return gson.toJson(updateNoteRequestModel);
    }

    @Test
    public void getNoteById() throws Exception {

        this.mockMvc.perform(put("/update-note").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", 2)));

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
                .andExpect(content().string("Note with given id(1000) does not exist "));
    }

    @Test
    public void addNote() throws Exception {
        AtomicReference<Integer> currentNubmerOfNotes = new AtomicReference<>();

        this.mockMvc.perform(get("/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    currentNubmerOfNotes.set(noteEntities.size());
                });

        this.mockMvc.perform(post("/add-note").header("Content-type", "application/json").content(createJsonOfNoteAddRequest()))
                .andExpect(status()
                        .isCreated());

        this.mockMvc.perform(get("/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(currentNubmerOfNotes.get() + 1, noteEntities.size());
                });
    }

    @Test
    public void deleteNoteWithId() throws Exception {
        AtomicReference<Integer> currentNubmerOfNotes = new AtomicReference<>();

        this.mockMvc.perform(get("/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    currentNubmerOfNotes.set(noteEntities.size());
                });

        this.mockMvc.perform(delete("/delete-note/2"))
                .andExpect(status()
                        .isOk());

        this.mockMvc.perform(get("/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(currentNubmerOfNotes.get() - 1, noteEntities.size());
                });

    }
}