package com.ms.cabmgmt.service;

import com.ms.cabmgmt.requests.BookingRequest;
import lombok.NonNull;

public interface BookingService {

    String book(@NonNull final BookingRequest bookingRequest);
}