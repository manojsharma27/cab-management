package com.ms.cabmgmt.service;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import lombok.NonNull;

import java.util.Optional;

public interface CabService {

    String register(@NonNull final CabRegistrationRequest request);

    Optional<Cab> get(@NonNull final String cabId);

    void updateLocation(@NonNull final String cabId, @NonNull final String location);

    void updateState(@NonNull String cabId, @NonNull CabState cabState);

    void updateAvailability(@NonNull String cabId, @NonNull Boolean availability);

}
