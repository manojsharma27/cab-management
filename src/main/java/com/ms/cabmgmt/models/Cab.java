package com.ms.cabmgmt.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ms.cabmgmt.enums.CabState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cab extends AuditableEntity {
    private final String driverName;
    private final String vehicleNumber;

    @Setter
    private String currentLocation;

    @Setter
    private CabState currentState;

    @Setter
    private boolean available;

    public Cab(String id, String driverName, String vehicleNumber) {
        super(id);
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.available = true;
    }

}
