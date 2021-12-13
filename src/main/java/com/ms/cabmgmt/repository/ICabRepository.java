package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.data.CabIdleDurationEntry;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ICabRepository {

    void save(Cab cab);

    Cab findById(String cabId);

    List<Cab> getAllCabs();

    List<Cab> getAvailableCabsByLocation(String location);

    void addIdleDuration(CabIdleDurationEntry cabIdleDurationEntry);

    void updateIdleDurationEnd(String cabId, Date endTime);

    List<CabIdleDurationEntry> getIdleDurationEntriesByCabId(String cabId);

    List<CabIdleDurationEntry> getIdleDurationEntriesInRange(Date fromTime, Date toTime);

    List<CabIdleDurationEntry> getIdleDurationEntriesInRangeForCabs(Set<String> cabIds, Date fromTime, Date toTime);

}
