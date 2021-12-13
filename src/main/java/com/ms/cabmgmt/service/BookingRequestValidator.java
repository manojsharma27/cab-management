package com.ms.cabmgmt.service;

import com.ms.cabmgmt.exceptions.CityNotSupportedException;
import com.ms.cabmgmt.models.City;
import com.ms.cabmgmt.repository.ICityRepository;
import com.ms.cabmgmt.requests.BookingRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class BookingRequestValidator {

    @Resource
    private ICityRepository onBoardedCityRepository;

    public void validate(BookingRequest bookingRequest) {
        Optional<City> optionalCity = onBoardedCityRepository.findByName(bookingRequest.getFromLocation());
        if (!optionalCity.isPresent()) {
            throw new CityNotSupportedException(bookingRequest.getFromLocation());
        }
    }
}
