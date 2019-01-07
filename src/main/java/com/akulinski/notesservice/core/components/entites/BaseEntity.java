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
class BaseEntity {

    @Column(name = "date_of_creation")
    @Temporal(TemporalType.DATE)
    private Date dateOfCreation;

    @Column(name = "date_of_modification")
    @Temporal(TemporalType.DATE)
    private Date dateOfModification;

}
