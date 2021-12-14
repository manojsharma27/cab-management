package com.ms.cabmgmt.service;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.data.CabTravelRecord;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CabService {

    String register(@NonNull final CabRegistrationRequest request);

    Optional<Cab> get(@NonNull final String cabId);

    void updateLocation(@NonNull final String cabId, @NonNull final String location);

    void updateState(@NonNull String cabId, @NonNull CabState cabState);

    void updateAvailability(@NonNull String cabId, @NonNull Boolean availability);

    long getCabIdleDuration(@NonNull String cabId, @NonNull Date fromTime, @NonNull Date toTime);

    List<CabTravelRecord> getCabHistoryInDuration(@NonNull String cabId, @NonNull Date fromTime, @NonNull Date toTime);

}
