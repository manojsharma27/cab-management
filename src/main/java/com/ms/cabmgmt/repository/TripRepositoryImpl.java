package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.Trip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TripRepositoryImpl implements ITripRepository {

    private Map<String, Trip> tripMap;

    public TripRepositoryImpl() {
        tripMap = new HashMap<>();
    }

    @Override
    public void save(Trip trip) {
        tripMap.put(trip.getId(), trip);
    }

    @Override
    public Trip findById(String tripId) {
        return tripMap.get(tripId);
    }
}
