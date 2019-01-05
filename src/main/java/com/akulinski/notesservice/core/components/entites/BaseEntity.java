package com.akulinski.notesservice.core.components.entites;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Embeddable
@MappedSuperclass
public class BaseEntity {

    @Column(name = "date_of_creation")
    private Date dateOfCreation;

    @Column(name = "date_of_modification")
    private Date dateOfModification;

    @PrePersist
    protected void onCreate() {
        dateOfCreation = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        dateOfModification = new Date();
    }
}
