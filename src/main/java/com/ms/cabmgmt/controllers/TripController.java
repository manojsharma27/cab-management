package com.ms.cabmgmt.controllers;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/v1/trips", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "trip-controller", description = "The trip related operations APIs")
public class TripController {

    // placeholder for trips
}
