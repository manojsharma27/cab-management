package com.ms.cabmgmt.integration;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HealthCheckServiceIT extends CabManagementITBase {

    @Before
    public void init() {
        setupBase();
    }

    @Test
    public void testHealthEndpoint() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url("/actuator/health"), String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("UP"));
    }
}
