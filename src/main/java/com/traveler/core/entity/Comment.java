package com.traveler.core.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="comment")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private int commentId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    public int getCommentId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public Board getBoard() {
        return board;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }
    public Comment(){}
    public Comment(User user, Board board, String content, LocalDateTime regDate) {
        this.user = user;
        this.board = board;
        this.content = content;
        this.regDate = regDate;
    }
}
