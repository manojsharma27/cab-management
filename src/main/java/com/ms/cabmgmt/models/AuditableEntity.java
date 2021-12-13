package com.ms.cabmgmt.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Getter
@NoArgsConstructor
@ToString
public abstract class AuditableEntity {

    private String id;
    private Date createdOn;

    public AuditableEntity(String id) {
        this.id = id;
        createdOn = new Date();
    }
}
