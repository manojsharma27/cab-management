package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.data.CabIdleDurationEntry;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CabRepositoryImpl implements ICabRepository {

    private Map<String, Cab> cabMap;
    private Map<String, List<CabIdleDurationEntry>> idleDurationMap;

    public CabRepositoryImpl() {
        cabMap = new HashMap<>();
        idleDurationMap = new HashMap<>();
    }

    @Override
    public void save(Cab cab) {
        cabMap.put(cab.getId(), cab);
    }

    @Override
    public Cab findById(String cabId) {
        return cabMap.get(cabId);
    }

    @Override
    public List<Cab> getAllCabs() {
        return new UnmodifiableList<>(new ArrayList<>(cabMap.values()));
    }

    @Override
    public List<Cab> getAvailableCabsByLocation(String location) {
        return cabMap.values().stream()
                .filter(cab -> cab.isAvailable() && cab.getCurrentState() == CabState.IDLE)
                .filter(cab -> location.equalsIgnoreCase(cab.getCurrentLocation()))
                .collect(Collectors.toList());
    }

    @Override
    public void addIdleDuration(CabIdleDurationEntry cabIdleDurationEntry) {
        String cabId = cabIdleDurationEntry.getCabId();
        idleDurationMap.computeIfAbsent(cabId, x -> new LinkedList<>());
        idleDurationMap.get(cabId).add(0, cabIdleDurationEntry);
    }

    @Override
    public void updateIdleDurationEnd(String cabId, Date endTime) {
        List<CabIdleDurationEntry> cabIdleDurationEntries = idleDurationMap.get(cabId);
        CabIdleDurationEntry mostRecentEntry = cabIdleDurationEntries.get(0);
        mostRecentEntry.setToTime(endTime);
    }

    @Override
    public List<CabIdleDurationEntry> getIdleDurationEntriesByCabId(String cabId) {
        return idleDurationMap.get(cabId);
    }

    @Override
    public List<CabIdleDurationEntry> getIdleDurationEntriesInRange(Date fromTime, Date toTime) {
        long from = fromTime.getTime();
        long to = toTime.getTime();
        return idleDurationMap.values().stream()
                .flatMap(Collection::stream)
                .filter(entry -> entry.getFromTime().getTime() >= from && entry.getToTime().getTime() <= to)
                .collect(Collectors.toList());
    }

    @Override
    public List<CabIdleDurationEntry> getIdleDurationEntriesInRangeForCabs(Set<String> cabIds, Date fromTime, Date toTime) {
        long from = fromTime.getTime();
        long to = toTime.getTime();
        return idleDurationMap.values().stream()
                .flatMap(Collection::stream)
                .filter(entry -> cabIds.contains(entry.getCabId()))
                .filter(entry -> entry.getFromTime().getTime() >= from && (entry.getToTime() == null || entry.getToTime().getTime() <= to))
                .collect(Collectors.toList());
    }
}