package com.traveler.core.repository;

import com.traveler.core.entity.Board;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findAllByDeletedDateIsNull(PageRequest pageRequest);
//    @Query("select b from Board b where ")
//    Page<Board> findAllBySearchWord(String searchWord,PageRequest pageRequest);
}