package com.ms.cabmgmt.controllers;

import com.ms.cabmgmt.service.CityOnBoardingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Validated
@RestController
@RequestMapping(value = "/v1/city", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "City onboarding APIs", description = "Provides API for onboarding city")
public class CityController {

    @Resource
    private CityOnBoardingService cityOnBoardingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity onBoard(@Validated @ApiParam(name = "city", required = true) @RequestParam String city) {
        cityOnBoardingService.onBoard(city);
        return ResponseEntity.ok("");
    }
}