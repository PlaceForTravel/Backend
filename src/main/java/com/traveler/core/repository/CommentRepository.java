package com.traveler.core.repository;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findCommentsByBoardBoardId(int boardId);
}
