package com.ms.cabmgmt.repository.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class CabIdleDurationEntry {

    private String cabId;
    private Date fromTime;
    @Setter private Date toTime;

}
