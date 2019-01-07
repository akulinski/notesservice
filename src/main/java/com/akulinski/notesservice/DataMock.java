package com.akulinski.notesservice;

import com.akulinski.notesservice.core.components.entites.HistoryEntity;
import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.HistoryRepository;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.akulinski.notesservice.utils.DateUtils;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.text.ParseException;

@Component
public class DataMock {

    @Value("${notes.mock.count}")
    private String mockCount;

    @Value("${notes.mock.words}")
    private String words;

    private final NotesRepository notesRepository;

    private final Lorem lorem;

    private Integer iterations;

    private Integer wordsIntValue;

    private final HistoryRepository historyRepository;

    private final SecureRandom secureRandom;

    @Autowired
    public DataMock(NotesRepository notesRepository, HistoryRepository historyRepository) {
        this.notesRepository = notesRepository;
        this.lorem = LoremIpsum.getInstance();

        this.historyRepository = historyRepository;
        secureRandom = new SecureRandom();
    }


    @PostConstruct
    public void initProperties() {
        this.iterations = Integer.parseInt(mockCount);
        this.wordsIntValue = Integer.parseInt(words);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void mockData() {

        for (int i = 0; i < iterations; i++) {
            createOneNoteAndSaveToDb();
        }

    }

    private void createOneNoteAndSaveToDb() {
        NoteEntity noteEntity = getNoteEntityAndSetValues();
        notesRepository.save(noteEntity);
    }

    private NoteEntity getNoteEntityAndSetValues() {
        HistoryEntity historyEntity = getHistoryEntityAndSaveToDb();
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setContent(lorem.getWords(wordsIntValue));
        noteEntity.setTitle(lorem.getWords(10));
        noteEntity.setHistoryEntity(historyEntity);
        try {
            noteEntity.setDateOfCreation(DateUtils.getFormattedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return noteEntity;
    }

    private HistoryEntity getHistoryEntityAndSaveToDb() {
        HistoryEntity historyEntity = new HistoryEntity();
        historyRepository.save(historyEntity);
        return historyEntity;
    }
}
