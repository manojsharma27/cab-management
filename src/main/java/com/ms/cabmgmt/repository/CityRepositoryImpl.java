package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.City;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CityRepositoryImpl implements ICityRepository {

    private Map<String, City> cityMap;

    public CityRepositoryImpl() {
        cityMap = new HashMap<>();
    }

    @Override
    public void save(City city) {
        cityMap.put(city.getId(), city);
    }

    @Override
    public City findById(String cityId) {
        return cityMap.get(cityId);
    }

    @Override
    public Optional<City> findByName(String name) {
        return cityMap.values().stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst();
    }
}
