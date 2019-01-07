package com.akulinski.notesservice.core.components.Controllers;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.models.requestmodels.NoteRequestModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HistoryControllerTest {

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

    @Test
    public void getHistory() throws Exception {

        AtomicReference<Integer> atomicReference = new AtomicReference<>();

        this.mockMvc.perform(get("/history/get-history/2").header("Content-type", "application/json")).andExpect(status().isOk()).andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    atomicReference.set(noteEntities.size());
                }
        );

        this.mockMvc.perform(put("/notes/update-note/2").header("Content-type", "application/json").content(createJsonOfNoteUpdateRequest("Mock value", "Mock")));
        this.mockMvc.perform(get("/history/get-history/2").header("Content-type", "application/json")).andExpect(status().isOk()).andExpect(mvcResult -> {
                    ArrayList<NoteEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<NoteEntity>().getClass());
                    assertEquals(atomicReference.get() + 1, noteEntities.size());
                }
        );

    }

    @Test
    public void idDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/history/get-history/200").header("Content-type", "application/json")).andExpect(status().isBadRequest());
    }


    @Test
    public void idOutOfRange() throws Exception {
        this.mockMvc.perform(get("/history/get-history/20000000000000000000000000000000000000000000").header("Content-type", "application/json")).andExpect(status().isBadRequest());
    }

    @Test
    public void getHistoryOfAll() throws Exception {

        this.mockMvc.perform(get("/history/get-full-history-of-all-notes").header("Content-type", "application/json")).andExpect(status().isOk()).andExpect(mvcResult -> {
                    ArrayList<HistoryEntity> noteEntities = gson.fromJson(mvcResult.getResponse().getContentAsString(), new ArrayList<HistoryEntity>().getClass());
                    assertEquals(30,noteEntities.size());
                }
        );
    }

}