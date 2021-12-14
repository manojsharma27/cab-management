package com.ms.cabmgmt.repository.data;

import com.ms.cabmgmt.enums.CabState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
public class CabTravelRecord {
    private String cabId;
    private Date date;
    private CabState state;
}
