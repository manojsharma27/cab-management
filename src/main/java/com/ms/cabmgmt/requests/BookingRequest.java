package com.ms.cabmgmt.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
public class BookingRequest {

    private String riderId;
    private String fromLocation;
    private String toLocation;
    private Date fromDate;
    private Date toDate;

}
