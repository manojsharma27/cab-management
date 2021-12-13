package com.ms.cabmgmt.strategies;

import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.requests.BookingRequest;
import lombok.NonNull;

import java.util.Set;

public interface CabSelectionStrategy {

    Cab selectCab(@NonNull BookingRequest bookingRequest, @NonNull Set<Cab> availableCabs);

}
