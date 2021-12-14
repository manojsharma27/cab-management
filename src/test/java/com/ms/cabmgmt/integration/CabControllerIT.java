package com.ms.cabmgmt.integration;

import com.ms.cabmgmt.controllers.CabController;
import com.ms.cabmgmt.controllers.CityController;
import com.ms.cabmgmt.enums.CabState;
import com.ms.cabmgmt.models.Cab;
import com.ms.cabmgmt.models.Rider;
import com.ms.cabmgmt.models.Trip;
import com.ms.cabmgmt.repository.ICabRepository;
import com.ms.cabmgmt.repository.IRiderRepository;
import com.ms.cabmgmt.repository.ITripRepository;
import com.ms.cabmgmt.repository.data.CabTravelRecord;
import com.ms.cabmgmt.requests.CabRegistrationRequest;
import com.ms.cabmgmt.service.CabService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CabControllerIT extends CabManagementITBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabControllerIT.class);

    @Resource
    private CabController cabController;

    @Resource
    private CityController cityController;

    @Resource
    private ICabRepository cabRepository;

    @Resource
    private IRiderRepository riderRepository;

    @Resource
    private ITripRepository tripRepository;

    @Resource
    private CabService cabService;

    @Before
    public void init() {
        setupBase();
    }

    @Test
    public void testCabRegistration() {
        ResponseEntity<String> registerRespEntity = cabController.register(getTestCabRegistrationRequest());
        assertNotNull(registerRespEntity.getBody());

        String cabId = registerRespEntity.getBody();
        LOGGER.info("Created cab : " + cabId);

        ResponseEntity<Object> cabResponseEntity = cabController.fetch(cabId);
        assertEquals(HttpStatus.OK, cabResponseEntity.getStatusCode());
        Cab cab = (Cab) cabResponseEntity.getBody();
        assertNotNull(cab);
        LOGGER.info("Fetched cab : " + cab);
    }

    @Test
    public void testCabBooking() {
        initCitiesAndCabs();

        List<String> cabIds = getCabIdsWithUpdatedLocation();

        cabController.updateAvailability(cabIds.get(0), false); // delhi
        cabController.updateAvailability(cabIds.get(3), false); // mumbai

        cabController.updateState(cabIds.get(3), CabState.ON_TRIP);
        cabController.updateState(cabIds.get(4), CabState.ON_TRIP);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        ResponseEntity<String> bookRespEntity = cabController.book("testId", "Mumbai", "Jaipur", new Date(), calendar.getTime());
        assertEquals(HttpStatus.OK, bookRespEntity.getStatusCode());

        String tripId = bookRespEntity.getBody();
        Trip trip = tripRepository.findById(tripId);
        assertNotNull(trip);
        assertNotNull(trip.getCab());
        assertEquals(cabIds.get(5), trip.getCab().getId());
    }


    private CabRegistrationRequest getTestCabRegistrationRequest() {
        return CabRegistrationRequest.builder()
                .driverName("Anil")
                .vehicleNumber("MH XY 1234")
                .build();
    }

    @Test
    public void testCabIdleDurationWithinGivenTime() {
        initCitiesAndCabs();

        List<String> cabIds = getCabIdsWithUpdatedLocation();

        cabController.updateAvailability(cabIds.get(0), false); // delhi
        cabController.updateAvailability(cabIds.get(3), false); // mumbai

        cabController.updateState(cabIds.get(3), CabState.ON_TRIP);
        cabController.updateState(cabIds.get(4), CabState.ON_TRIP);

        long cabIdleDuration = cabService.getCabIdleDuration(cabIds.get(6), getCustomDate(-2), new Date());
        System.out.println("Total time (ms) cab " + cabIds.get(6) + " was idle is : " + cabIdleDuration);
    }

    @Test
    public void testCabHistoryWithinGivenTime() {
        initCitiesAndCabs();

        List<String> cabIds = getCabIdsWithUpdatedLocation();

        cabController.updateAvailability(cabIds.get(0), false); // delhi
        cabController.updateAvailability(cabIds.get(3), false); // mumbai

        cabController.updateState(cabIds.get(3), CabState.ON_TRIP);
        cabController.updateState(cabIds.get(4), CabState.ON_TRIP);
        cabController.updateState(cabIds.get(0), CabState.ON_TRIP);
        cabController.updateState(cabIds.get(3), CabState.IDLE);
        cabController.updateState(cabIds.get(0), CabState.IDLE);
        cabController.updateState(cabIds.get(3), CabState.ON_TRIP);

        List<CabTravelRecord> cabHistory = cabService.getCabHistoryInDuration(cabIds.get(3), getCustomDate(-2), new Date());
        cabHistory.forEach(System.out::println);
    }

    @After
    public void tearDown() {
        cleanupAll();
    }

    private Date getCustomDate(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

    private List<String> getCabIdsWithUpdatedLocation() {
        List<Cab> allCabs = cabRepository.getAllCabs();
        LOGGER.info("Created cabs:");
        allCabs.forEach(cab -> LOGGER.info("{}", cab));
        List<String> cabIds = allCabs.stream().map(c -> c.getId()).collect(Collectors.toList());

        cabController.updateLocation(cabIds.get(0), "Delhi");
        cabController.updateLocation(cabIds.get(1), "Pune");
        cabController.updateLocation(cabIds.get(2), "Goa");
        cabController.updateLocation(cabIds.get(3), "Mumbai");
        cabController.updateLocation(cabIds.get(4), "Mumbai");
        cabController.updateLocation(cabIds.get(5), "Mumbai");
        cabController.updateLocation(cabIds.get(6), "Mumbai");
        return cabIds;
    }

    private void initCitiesAndCabs() {
        cityController.onBoard("Mumbai");
        cityController.onBoard("Delhi");
        cityController.onBoard("Pune");
        cityController.onBoard("Goa");

        ResponseEntity<String> registerRespEntity = cabController.register(getTestCabRegistrationRequest());
        assertEquals(HttpStatus.OK, registerRespEntity.getStatusCode());

        riderRepository.save(new Rider("testId", "GhostRider", "1234567890"));

        Stream.of(
                CabRegistrationRequest.builder().driverName("Anil").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Enna").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Meena").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Dika").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Daaye").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Damma").vehicleNumber("MH XY 1234").build(),
                CabRegistrationRequest.builder().driverName("Nika").vehicleNumber("MH XY 1234").build())
                .forEach(request -> {
                    cabController.register(request);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                });
    }
}
