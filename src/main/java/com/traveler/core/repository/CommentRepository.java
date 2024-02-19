package com.traveler.core.repository;
import com.traveler.core.entity.Board;
import com.traveler.core.entity.User;
import org.springframework.data.domain.Page;
import com.traveler.core.entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Page<Comment> findCommentsByBoardBoardId(int boardId, PageRequest pageRequest);
    void deleteAllByUser(User user);
    void deleteAllByBoard(Board board);
}
