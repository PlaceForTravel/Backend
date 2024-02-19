package com.traveler.core.repository;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.SavedBoardPlace;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedBoardPlaceRepository extends JpaRepository<SavedBoardPlace,Integer> {
    @Query("select sb from SavedBoardPlace sb where sb.user = :user AND sb.boardPlace.board.cityName = :cityName")
    Page<SavedBoardPlace> findAllByUserAndCityName(@Param("user") User user,@Param("cityName") String cityName, PageRequest pageRequest);

    @Query("SELECT DISTINCT sb.boardPlace.board.cityName FROM SavedBoardPlace sb WHERE sb.user = :user")
    List<String> findCityName(@Param("user") User user);

    boolean existsByUserAndBoardPlace(User user, BoardPlace boardPlace);

    void deleteAllByUser(User user);
}
