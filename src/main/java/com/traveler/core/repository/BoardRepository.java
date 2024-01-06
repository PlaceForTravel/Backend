package com.traveler.core.repository;

import com.traveler.core.entity.Board;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findAllByCityName(String cityName, PageRequest pageRequest); // 페이지로 가져올려면 List가 아닌 Page로
    Page<Board> findAllByDeletedDateIsNull(PageRequest pageRequest);

}