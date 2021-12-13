package com.ms.cabmgmt.service;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.exceptions.CabAlreadyExistsException;
import com.ms.cabmgmt.exceptions.CabNotFoundException;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.ICabRepository;
import com.ms.cabmgmt.repository.data.CabIdleDurationEntry;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CabServiceImpl implements CabService {

    @Resource
    private ICabRepository cabRepository;

    @Override
    public String register(@NonNull CabRegistrationRequest request) {
        if (Objects.nonNull(request.getCabId()) && get(request.getCabId()).isPresent()) {
            throw new CabAlreadyExistsException(request.getCabId());
        }
        String cabId = StringUtils.isBlank(request.getCabId()) ? UUID.randomUUID().toString() : request.getCabId();
        Cab cab = new Cab(cabId, request.getDriverName(), request.getVehicleNumber());
        cab.setCurrentState(CabState.IDLE);
        cab.setAvailable(true);
        cabRepository.save(cab);
        cabRepository.addIdleDuration(new CabIdleDurationEntry(cabId, new Date(), null));
        return cabId;
    }

    @Override
    public Optional<Cab> get(@NonNull String cabId) {
        return Optional.of(cabRepository.findById(cabId));
    }

    @Override
    public void updateLocation(@NonNull String cabId, @NonNull String location) {
        Cab cab = fetchCab(cabId);
        cab.setCurrentLocation(location);
        cabRepository.save(cab);
    }

    @Override
    public void updateState(@NonNull String cabId, @NonNull CabState newState) {
        Cab cab = fetchCab(cabId);
        if (cab.getCurrentState() == newState) {
            return;
        }
        cab.setCurrentState(newState);
        cabRepository.save(cab);
        if (newState == CabState.IDLE) {
            cabRepository.addIdleDuration(new CabIdleDurationEntry(cabId, new Date(), null));
        } else {
            cabRepository.updateIdleDurationEnd(cabId, new Date());
        }
    }

    @Override
    public void updateAvailability(@NonNull String cabId, @NonNull Boolean availability) {
        Cab cab = fetchCab(cabId);
        if (cab.isAvailable() == availability) {
            return;
        }
        cab.setAvailable(availability);
        cabRepository.save(cab);
    }

    private Cab fetchCab(@NonNull String cabId) {
        Cab cab = cabRepository.findById(cabId);
        if (cab == null) {
            throw new CabNotFoundException(cabId);
        }
        return cab;
    }

}
