package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.Trip;

public interface ITripRepository {

    void save(Trip trip);

    Trip findById(String tripId);

}
