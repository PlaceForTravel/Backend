package com.traveler.core.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_board")
public class SavedBoard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAVEDBOARD_ID")
    private int savedBoardId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    public int getSavedBoardId() {
        return savedBoardId;
    }

    public User getUser() {
        return user;
    }

    public Board getBoard() {
        return board;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }
    public SavedBoard(){}
    public SavedBoard(Board board, User user, LocalDateTime regDate){
        this.board = board;
        this.user = user;

        this.regDate = regDate;
    }

}
