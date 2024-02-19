package com.traveler.core.repository;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Integer> {

    void deleteAllByBoardPlace(BoardPlace boardPlace);
}
