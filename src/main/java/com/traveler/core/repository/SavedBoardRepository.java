package com.traveler.core.repository;

import com.traveler.core.entity.Board;
import com.traveler.core.entity.SavedBoard;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface SavedBoardRepository extends JpaRepository<SavedBoard, Integer> {
    Page<SavedBoard> findAllByUser(User user, PageRequest pageRequest);

    Optional<SavedBoard> findByUserAndBoard(User user, Board board);

    @Query("SELECT sb FROM SavedBoard sb WHERE sb.user = :user AND sb.board = :board AND sb.board.deletedDate IS NULL")
    Optional<SavedBoard> findByUserAndBoard_NotDeleted(@Param("user") User user, @Param("board") Board board);
}
