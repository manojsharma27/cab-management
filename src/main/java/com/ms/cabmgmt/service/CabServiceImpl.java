package com.ms.cabmgmt.service;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.exceptions.CabAlreadyExistsException;
import com.ms.cabmgmt.exceptions.CabNotFoundException;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.ICabRepository;
import com.ms.cabmgmt.repository.data.CabIdleDurationEntry;
import com.ms.cabmgmt.repository.data.CabTravelRecord;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        Date currDate = new Date();
        if (newState == CabState.IDLE) {
            cabRepository.addIdleDuration(new CabIdleDurationEntry(cabId, currDate, null));
        } else {
            cabRepository.updateIdleDurationEnd(cabId, currDate);
        }
        cabRepository.addCabTravelRecord(cab.getId(), newState, currDate);
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

    @Override
    public long getCabIdleDuration(@NonNull String cabId, @NonNull Date fromTime, @NonNull Date toTime) {
        Set<String> set = new HashSet<>();
        set.add(cabId);
        List<CabIdleDurationEntry> idleDurationEntries = cabRepository.getIdleDurationEntriesInRangeForCabs(set, fromTime, toTime);
        long totalIdleDuration = 0;
        for (CabIdleDurationEntry entry : idleDurationEntries) {
            long entryToTime = entry.getToTime() == null ? System.currentTimeMillis() : entry.getToTime().getTime();
            totalIdleDuration += entryToTime - entry.getFromTime().getTime();
        }
        return totalIdleDuration;
    }

    @Override
    public List<CabTravelRecord> getCabHistoryInDuration(@NonNull String cabId, @NonNull Date fromTime, @NonNull Date toTime) {
        Set<String> set = new HashSet<>();
        set.add(cabId);
        return cabRepository.getHistoryForCabsInDateRange(set, fromTime, toTime);
    }

    private Cab fetchCab(@NonNull String cabId) {
        Cab cab = cabRepository.findById(cabId);
        if (cab == null) {
            throw new CabNotFoundException(cabId);
        }
        return cab;
    }

}
