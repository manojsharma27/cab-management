package com.ms.cabmgmt.service;

import com.ms.cabmgmt.exceptions.CityAlreadyOnBoardedException;
import com.ms.cabmgmt.models.City;
import com.ms.cabmgmt.repository.ICityRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

@Service
public class CityOnBoardingServiceImpl implements CityOnBoardingService {

    @Resource
    private ICityRepository cityRepository;

    @Override
    public void onBoard(@NonNull String cityName) {
        Optional<City> optionalCity = cityRepository.findByName(cityName);
        if (optionalCity.isPresent()) {
            throw new CityAlreadyOnBoardedException(cityName);
        }

        cityRepository.save(new City(UUID.randomUUID().toString(), cityName));
    }
}
