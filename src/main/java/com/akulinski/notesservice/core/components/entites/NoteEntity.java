package com.akulinski.notesservice.core.components.entites;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
public class NoteEntity extends BaseEntityWithVersioning {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_note")
    private Integer id;

    @Column(name = "note_content")
    private String content;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;
}
