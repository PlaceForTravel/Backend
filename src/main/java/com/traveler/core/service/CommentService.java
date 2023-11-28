package com.traveler.core.service;

import com.traveler.core.dto.CommentRequestDTO;
import com.traveler.core.dto.CommentResponseDTO;
import com.traveler.core.entity.Board;
import com.traveler.core.entity.Comment;
import com.traveler.core.entity.User;
import com.traveler.core.repository.BoardRepository;
import com.traveler.core.repository.CommentRepository;
import com.traveler.core.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

        private final CommentRepository commentRepository;
        private final BoardRepository boardRepository;
        private final UserRepository userRepository;

        public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository) {
            this.commentRepository = commentRepository;
            this.boardRepository = boardRepository;
            this.userRepository = userRepository;
        }

        public Page<CommentResponseDTO> paging(Pageable pageable, int boardId){
            int page = pageable.getPageNumber()-1; // 현재페이지 반환
            int pagelimit = 10;

            Page<Comment> commentPages = commentRepository.findAll(PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

            Page<CommentResponseDTO> commentResponseDTOS = commentPages.map(
                    commentPage -> new CommentResponseDTO(commentPage));

            return commentResponseDTOS;
        }

        public void saveComment(int boardId, CommentRequestDTO commentRequestDTO){
            Board board = boardRepository.findById(boardId).orElse(null);
            User user = userRepository.findById(commentRequestDTO.getUserId()).orElse(null);

            Comment comment = new Comment(user, board, commentRequestDTO.getContent(), LocalDateTime.now());
            commentRepository.save(comment);

        }

        public void deleteComment(int commentId){
            Comment comment = commentRepository.findById(commentId).orElse(null);
            commentRepository.delete(comment);
        }

}
