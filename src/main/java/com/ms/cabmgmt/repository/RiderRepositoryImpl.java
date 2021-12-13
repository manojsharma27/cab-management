package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.Rider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RiderRepositoryImpl implements IRiderRepository {

    private Map<String, Rider> riderMap;

    public RiderRepositoryImpl() {
        riderMap = new HashMap<>();
    }

    @Override
    public void save(Rider rider) {
        riderMap.put(rider.getId(), rider);
    }

    @Override
    public Rider findById(String riderId) {
        return riderMap.get(riderId);
    }
}
