package com.traveler.core.repository;

import com.traveler.core.entity.Board;
import com.traveler.core.entity.SavedBoard;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedBoardRepository extends JpaRepository<SavedBoard,Integer> {
    Page<SavedBoard> findAllByUser(User user, PageRequest pageRequest);
}
