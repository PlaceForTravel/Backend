package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.Comment;

import java.sql.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CommentResponseDTO {

    private int commentId;
    private String userId;
    private String nickname;
    private String content;
    private String regDate;
    private boolean commented;

    public CommentResponseDTO(Comment comment, boolean commented){
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getUserId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.regDate = comment.getRegDate().toString();
        this.commented = commented;
    }
}
