package com.akulinski.notesservice.models.requestmodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoteRequestModel {
    private String content;
    private String title;

}
