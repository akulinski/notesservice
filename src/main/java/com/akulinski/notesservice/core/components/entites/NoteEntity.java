package com.akulinski.notesservice.core.components.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.PreDestroy;
import javax.persistence.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
public class NoteEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_note")
    private Integer id;

    @Column(name = "note_content")
    private String content;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "is_current")
    private Boolean isCurrent = Boolean.TRUE;

    @Column(name = "entity_version")
    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id_history")
    @JsonIgnore
    private HistoryEntity historyEntity;

    @PreDestroy
    public void deleteNote() {
        this.isDeleted = Boolean.TRUE;
    }


    public void isNoLongerCurrent() {
        this.isCurrent = Boolean.FALSE;
    }

    public void incrementVersion() {
        this.version++;
    }
}
