package com.traveler.core.repository;

import com.traveler.core.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place,Integer> {
    Place findByAddress(String address);
}
