package com.ms.cabmgmt.repository;

import com.ms.cabmgmt.models.Rider;

public interface IRiderRepository {

    void save(Rider rider);

    Rider findById(String riderId);

}
