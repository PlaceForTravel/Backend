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
    private final CommentService commentService;

    public CommentController( CommentService commentService) {
        this.commentService = commentService;

    }

    @GetMapping(value = "/board/{boardId}/comment")
    public Page<CommentResponseDTO> commentList(@PageableDefault(page = 1) Pageable pageable, @PathVariable int boardId, @RequestParam String userId){
        Page<CommentResponseDTO> commentResponseDTOList = commentService.paging(pageable, boardId, userId);
        return commentResponseDTOList;
    }
    @PostMapping(value = "/board/{boardId}/comment/save")
    public void saveComment(@PathVariable int boardId, @RequestBody CommentRequestDTO commentRequestDTO){
        commentService.saveComment(boardId, commentRequestDTO);
        commentService.commentNoti(boardId, commentRequestDTO);
    }
    @DeleteMapping(value = "/board/comment/delete/{commentId}")
    public void deleteComment(@PathVariable int commentId, @RequestParam String userId){
        commentService.deleteComment(commentId,userId);}
}
