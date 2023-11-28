package com.traveler.core.repository;

import com.traveler.core.entity.BoardPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardPlaceRepository extends JpaRepository<BoardPlace,Integer> {
    List<BoardPlace> findBoardPlacesByBoardBoardId(int boardId);
    Page<BoardPlace> findBoardPlaceByPlacePlaceName(String placeName, PageRequest pageRequest);
}
