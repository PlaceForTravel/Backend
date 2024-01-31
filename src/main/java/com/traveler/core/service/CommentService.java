package com.traveler.core.service;

import com.traveler.core.dto.CommentRequestDTO;
import com.traveler.core.dto.CommentResponseDTO;
import com.traveler.core.dto.FCMNotificationRequestDTO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FCMNotificationService fcmNotificationService;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository, FCMNotificationService fcmNotificationService) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.fcmNotificationService = fcmNotificationService;
    }

    public Page<CommentResponseDTO> paging(Pageable pageable, int boardId, String userId) {
        int page = pageable.getPageNumber() - 1; // 현재페이지 반환
        int pagelimit = 10;

        Page<Comment> commentPages = commentRepository.findCommentsByBoardBoardId(boardId, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC, "regDate")));
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Page<CommentResponseDTO> commentResponseDTOS = commentPages.map(
                    commentPage -> {
                        if (commentPage.getUser().getUserId().equals(userId)) {
                            return new CommentResponseDTO(commentPage, true);
                        } else return new CommentResponseDTO(commentPage, false);
                    });

            return commentResponseDTOS;
        }
        Page<CommentResponseDTO> commentResponseDTOS = commentPages.map(
                commentPage ->
                        new CommentResponseDTO(commentPage, false));
        return commentResponseDTOS;
    }

    public void saveComment(int boardId, CommentRequestDTO commentRequestDTO) {
        Board board = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findById(commentRequestDTO.getUserId()).orElse(null);

        Comment comment = new Comment(user, board, commentRequestDTO.getContent(), LocalDateTime.now());
        commentRepository.save(comment);

    }

    public void deleteComment(int commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment.getUser().getUserId().equals(userId)) {
            commentRepository.delete(comment);
        } else {
            //오류 날리기
        }
    }

    public void commentNoti(int boardId, CommentRequestDTO commentRequestDTO) {
        Optional<Board> board = boardRepository.findById(boardId);
        Map<String, String> data = new HashMap<>();
        data.put("boardId", Integer.toString(boardId));
        FCMNotificationRequestDTO fcmNotificationRequestDTO = FCMNotificationRequestDTO.builder()
                .body(commentRequestDTO.getNickname() + "님이 당신의 게시물에 댓글을 달았습니다.")
                .title("댓글")
                .userId(board.get().getUser().getUserId())
                .data(data)
                .build();
        fcmNotificationService.sendNotificationByToken(fcmNotificationRequestDTO);
    }

}
