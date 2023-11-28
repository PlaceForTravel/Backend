package com.traveler.core.repository;

import com.traveler.core.entity.SavedBoardPlace;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedBoardPlaceRepository extends JpaRepository<SavedBoardPlace,Integer> {
    Page<SavedBoardPlace> findAllByUser(User user, PageRequest pageRequest);
}
