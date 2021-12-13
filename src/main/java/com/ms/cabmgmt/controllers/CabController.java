package com.ms.cabmgmt.controllers;

import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.requests.BookingRequest;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import com.ms.cabmgmt.service.BookingService;
import com.ms.cabmgmt.service.CabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = "/v1/cabs", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "cab-controller", description = "The cab related operations APIs")
public class CabController {

    @Resource
    private CabService cabService;

    @Resource
    private BookingService bookingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> register(@Validated @ApiParam(name = "cabRegistrationRequest", required = true) @RequestBody CabRegistrationRequest request) {
        String cabId = cabService.register(request);
        return ResponseEntity.ok(cabId);
    }

    @RequestMapping(value = "/{cabId}", method = RequestMethod.GET)
    public ResponseEntity fetch(@NotEmpty @ApiParam(name = "cabId", required = true) @PathVariable(value = "cabId") String cabId) {
        final Optional<Cab> optionalCab = cabService.get(cabId);
        return ResponseEntity.of(optionalCab);
    }

    @RequestMapping(value = "/{cabId}/updateLocation", method = RequestMethod.PUT)
    public ResponseEntity updateLocation(@NotEmpty @ApiParam(name = "cabId", required = true) @PathVariable(value = "cabId") String cabId,
                                         @NotEmpty @ApiParam(name = "location", required = true) @RequestParam(value = "location") String location) {
        cabService.updateLocation(cabId, location);
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/{cabId}/updateAvailability", method = RequestMethod.PUT)
    public ResponseEntity updateAvailability(@NotEmpty @ApiParam(name = "cabId", required = true) @PathVariable(value = "cabId") String cabId,
                                             @NotNull @ApiParam(name = "available", required = true) @RequestParam(value = "available") Boolean available) {
        cabService.updateAvailability(cabId, available);
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/{cabId}/updateState", method = RequestMethod.PUT)
    public ResponseEntity updateState(@NotEmpty @ApiParam(name = "cabId", required = true) @PathVariable(value = "cabId") String cabId,
                                      @NotNull @ApiParam(name = "state", required = true) @RequestParam(value = "state") CabState state) {
        cabService.updateState(cabId, state);
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<String> book(@NotEmpty @ApiParam(name = "riderId", required = true) @RequestParam(value = "riderId") String riderId,
                                       @NotEmpty @ApiParam(name = "fromLocation", required = true) @RequestParam(value = "fromLocation") String fromLocation,
                                       @NotEmpty @ApiParam(name = "toLocation", required = true) @RequestParam(value = "toLocation") String toLocation,
                                       @NotNull @ApiParam(name = "fromDate", required = true) @RequestParam(value = "fromDate") Date fromDate,
                                       @NotNull @ApiParam(name = "toDate", required = true) @RequestParam(value = "toDate") Date toDate) {
        BookingRequest bookingRequest = BookingRequest.builder()
                .riderId(riderId)
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        String tripId = bookingService.book(bookingRequest);
        return ResponseEntity.ok(tripId);
    }

}