package com.akulinski.notesservice;

import com.akulinski.notesservice.core.components.entites.NoteEntity;
import com.akulinski.notesservice.core.components.repositories.NotesRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    @Autowired
    public DataMock(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
        this.lorem = LoremIpsum.getInstance();

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
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setContent(lorem.getWords(wordsIntValue));
        notesRepository.save(noteEntity);
    }
}
