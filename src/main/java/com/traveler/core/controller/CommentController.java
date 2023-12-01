package com.traveler.core.controller;

import com.traveler.core.dto.CommentRequestDTO;
import com.traveler.core.dto.CommentResponseDTO;
import com.traveler.core.dto.FCMNotificationRequestDTO;
import com.traveler.core.entity.Board;
import com.traveler.core.repository.BoardRepository;
import com.traveler.core.repository.CommentRepository;
import com.traveler.core.service.CommentService;
import com.traveler.core.service.FCMNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final BoardRepository boardRepository;
    private final FCMNotificationService fcmNotificationService;

    public CommentController(CommentRepository commentRepository, CommentService commentService, BoardRepository boardRepository, FCMNotificationService fcmNotificationService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.boardRepository = boardRepository;
        this.fcmNotificationService = fcmNotificationService;
    }

    @GetMapping(value = "/board/{boardId}/comment")
    public Page<CommentResponseDTO> boardList(@PageableDefault(page = 1) Pageable pageable, @PathVariable int boardId){
        Page<CommentResponseDTO> commentResponseDTOList = commentService.paging(pageable, boardId);
        return commentResponseDTOList;
    }
    @PostMapping(value = "/board/{boardId}/comment/save")
    public void saveComment(@PathVariable int boardId, @RequestBody CommentRequestDTO commentRequestDTO){
        commentService.saveComment(boardId, commentRequestDTO);
        Optional<Board> board = boardRepository.findById(boardId);
        Map<String,String > data = new HashMap<String,String>();
        data.put("boardId",Integer.toString(boardId));
        FCMNotificationRequestDTO fcmNotificationRequestDTO = FCMNotificationRequestDTO.builder()
                .body(commentRequestDTO.getUserId()+ "님이 당신의 게시물에 댓글을 달았습니다.")
                .title("댓글")
                .userId(board.get().getUser().getUserId())
                .data(data)
                .build();
        fcmNotificationService.sendNotificationByToken(fcmNotificationRequestDTO);
    }
    @DeleteMapping(value = "/board/comment/delete/{commentId}")
    public void deleteComment(@PathVariable int commentId){
        commentService.deleteComment(commentId);}
}
