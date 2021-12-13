package com.ms.cabmgmt.service;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.exceptions.NoCabsAvailableException;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.models.Rider;
import com.ms.cabmgmt.models.Trip;
import com.ms.cabmgmt.repository.ICabRepository;
import com.ms.cabmgmt.repository.IRiderRepository;
import com.ms.cabmgmt.repository.ITripRepository;
import com.ms.cabmgmt.requests.BookingRequest;
import com.ms.cabmgmt.strategies.CabSelectionStrategy;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Resource
    private ICabRepository cabRepository;

    @Resource
    private CabService cabService;

    @Resource
    private CabSelectionStrategy mostIdleCabSelectionStrategy;

    @Resource
    private IRiderRepository riderRepository;

    @Resource
    private ITripRepository tripRepository;

    @Resource
    private BookingRequestValidator bookingRequestValidator;

    @Override
    public String book(@NonNull BookingRequest bookingRequest) {
        bookingRequestValidator.validate(bookingRequest);
        final String fromLocation = bookingRequest.getFromLocation();
        List<Cab> availableCabs = cabRepository.getAvailableCabsByLocation(fromLocation);
        if (CollectionUtils.isEmpty(availableCabs)) {
            throw new NoCabsAvailableException();
        }

        Cab cab = mostIdleCabSelectionStrategy.selectCab(bookingRequest, new LinkedHashSet<>(availableCabs));
        return performBooking(cab.getId(), bookingRequest);
    }

    private String performBooking(String cabId, BookingRequest bookingRequest) {
        cabService.updateAvailability(cabId, false);
        cabService.updateState(cabId, CabState.ON_TRIP);
        cabRepository.updateIdleDurationEnd(cabId, new Date());
        Rider rider = riderRepository.findById(bookingRequest.getRiderId());
        Trip trip = Trip.builder()
                .id(UUID.randomUUID().toString())
                .createdOn(new Date())
                .cab(cabRepository.findById(cabId))
                .rider(rider)
                .fromTime(bookingRequest.getFromDate())
                .toTime(bookingRequest.getToDate())
                .from(bookingRequest.getFromLocation())
                .to(bookingRequest.getToLocation())
                .build();
        tripRepository.save(trip);
        LOGGER.info("Booking confirmed. Trip details : {}", trip);
        return trip.getId();
    }
}