package com.ms.cabmgmt.strategies;

import com.ms.cabmgmt.exceptions.CabNotFoundException;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.repository.ICabRepository;
import com.ms.cabmgmt.repository.data.CabIdleDurationEntry;
import com.ms.cabmgmt.requests.BookingRequest;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MostIdleCabSelectionStrategyImpl implements CabSelectionStrategy {

    private static int DEFAULT_IDLE_DURATION_DAYS = -5;

    @Resource
    private ICabRepository cabRepository;

    @Override
    public Cab selectCab(@NonNull BookingRequest bookingRequest, @NonNull Set<Cab> availableCabs) {
        Set<String> cabIds = availableCabs.stream().map(Cab::getId).collect(Collectors.toSet());
        List<CabIdleDurationEntry> durationEntries = cabRepository.getIdleDurationEntriesInRangeForCabs(cabIds, getFromTime(), new Date());

        Map<String, Long> idleDurationMap = populateIdleDurationMap(durationEntries);

        List<String> idleCabIds = new ArrayList<>();
        long maxIdleDuration = -1;

        for (Map.Entry<String, Long> entry : idleDurationMap.entrySet()) {
            String cabId = entry.getKey();
            Long duration = entry.getValue();
            if (duration >= maxIdleDuration) {
                idleCabIds.add(cabId);
                maxIdleDuration = duration;
            }
        }

        if (maxIdleDuration == -1) {
            throw new CabNotFoundException("Couldn't find any Idle Cab for booking");
        }

        if (idleCabIds.size() == 1) {
            return cabRepository.findById(idleCabIds.get(0));
        }

        int randomIndex = (int) (Math.random() * idleCabIds.size());
        return cabRepository.findById(idleCabIds.get(randomIndex));
    }

    private Map<String, Long> populateIdleDurationMap(List<CabIdleDurationEntry> durationEntries) {
        Map<String, Long> idleDurationMap = new HashMap<>();
        for (CabIdleDurationEntry entry : durationEntries) {
            long previous = idleDurationMap.getOrDefault(entry.getCabId(), 0L);
            long toTime = entry.getToTime() == null ? System.currentTimeMillis() : entry.getToTime().getTime();
            idleDurationMap.put(entry.getCabId(), previous + toTime - entry.getFromTime().getTime());
        }
        return idleDurationMap;
    }

    private Date getFromTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, DEFAULT_IDLE_DURATION_DAYS);
        return calendar.getTime();
    }
}
