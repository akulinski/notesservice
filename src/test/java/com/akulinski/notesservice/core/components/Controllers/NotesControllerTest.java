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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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

    private final static String EXPECTED_CONTENT_AFTER_UPDATE = "Note with id 2 was updated";

    @Value("${notes.mock.count}")
    private String exptectedNumberOfNotes;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gson = new GsonBuilder().create();
    }

    private String createJsonOfNoteUpdateRequest(String content, String title) {
        NoteRequestModel updateNoteRequestModel = new NoteRequestModel();
        updateNoteRequestModel.setContent(content);
        updateNoteRequestModel.setTitle(title);

        return gson.toJson(updateNoteRequestModel);
    }

    private String createJsonOfNoteAddRequest() {

        NoteRequestModel updateNoteRequestModel = new NoteRequestModel();

        updateNoteRequestModel.setContent("Mock value");
        updateNoteRequestModel.setTitle("Mock Title");
        return gson.toJson(updateNoteRequestModel);
    }


    @Test
    public void updateNote() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock")))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .string(containsString(EXPECTED_CONTENT_AFTER_UPDATE)));
    }

    @Test
    public void updateNoteMultipleTimesWithSameValues() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")));

        Integer currentversion = notesRepository.findById(2).get().getVersion();

        for (int i = 0; i < 10; i++) {
            this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")));
        }

        Integer afterupdates = notesRepository.findById(2).get().getVersion();

        assertEquals(currentversion, afterupdates);
    }

    @Test
    public void updateNoteAndCheckTitle() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")))
                .andExpect(status()
                        .isOk());
        this.mockMvc.perform(get("/notes/get-note-by-id/2"))
                .andExpect(status()
                        .isOk())
                .andExpect(content().string(containsString("Mock Title")));
    }

    @Test
    public void updateNoteWithEmptyTitle() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "")))
                .andExpect(status()
                        .isBadRequest());
    }


    @Test
    public void updateNoteWithEmptyContent() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("", "Mock Title")))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .string("Cannot assign empty content to note"));
    }

    @Test
    public void updateNoteWithIdOutOfRange() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/10000000000000000000000").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void updateNoteThatDoesntExist() throws Exception {
        this.mockMvc.perform(put("/notes/update-note/100").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content()
                        .string("Note with given id(100) does not exist "));
    }

    @Test
    public void getNoteById() throws Exception {

        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock Title")));

        this.mockMvc.perform(get("/notes/get-note-by-id/2"))
                .andExpect(status()
                        .isOk())
                .andExpect(content().string(containsString("Mock value")));
    }

    @Test
    public void getNoteByIdThatIsOutOfRange() throws Exception {
        this.mockMvc.perform(get("/notes/get-note-by-id/10000000000000000"))
                .andExpect(status()
                        .isBadRequest());
    }


    @Test
    public void getNoteByIdThatDosntExists() throws Exception {
        this.mockMvc.perform(get("/notes/get-note-by-id/1000"))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(content().string("Note with given id(1000) does not exist "));
    }

    @Test
    public void addNote() throws Exception {
        AtomicReference<Integer> currentNumberOfNotes = new AtomicReference<>();

        this.mockMvc.perform(get("/notes/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    currentNumberOfNotes.set(noteEntities.size());
                });

        this.mockMvc.perform(post("/notes/add-note").header("Content-type", "application/json").content(createJsonOfNoteAddRequest()))
                .andExpect(status()
                        .isCreated());

        this.mockMvc.perform(get("/notes/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(currentNumberOfNotes.get() + 1, noteEntities.size());
                });
    }

    @Test
    public void deleteNoteWithId() throws Exception {
        AtomicReference<Integer> currentNumberOfNotes = new AtomicReference<>();

        this.mockMvc.perform(get("/notes/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    currentNumberOfNotes.set(noteEntities.size());
                });

        this.mockMvc.perform(delete("/notes/delete-note/6"))
                .andExpect(status()
                        .isOk());

        this.mockMvc.perform(get("/notes/get-all"))
                .andExpect(status()
                        .isOk())
                .andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(currentNumberOfNotes.get() - 1, noteEntities.size());
                });

    }
}