package com.akulinski.notesservice.models.responsemodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoteUpdateResponseModel {
    private String message;

    public NoteUpdateResponseModel(String message) {
        this.message = message;
    }
}
