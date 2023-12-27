package com.traveler.core.repository;
import org.springframework.data.domain.Page;
import com.traveler.core.entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Page<Comment> findCommentsByBoardBoardId(int boardId, PageRequest pageRequest);
}
