package com.ms.cabmgmt.models;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class Trip extends AuditableEntity {

    private final Rider rider;
    private final Cab cab;
    private final String from;
    private final String to;
    private final Date fromTime;
    private final Date toTime;

}