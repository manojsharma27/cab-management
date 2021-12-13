package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.City;

import java.util.Optional;

public interface ICityRepository {

    void save(City city);

    City findById(String Id);

    Optional<City> findByName(String name);

}
