package ru.atc.bclient.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserLegalEntityPK implements Serializable {

    @Getter @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Getter @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private LegalEntity legalEntity;
}
