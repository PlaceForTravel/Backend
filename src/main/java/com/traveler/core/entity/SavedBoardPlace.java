package com.traveler.core.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_board_place")
public class SavedBoardPlace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAVEDBOARDPLACE_ID")
    private int savedPlaceId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "BOARDPLACE_ID")
    private BoardPlace boardPlace;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;


    public int getSavedPlaceId() {
        return savedPlaceId;
    }

    public User getUser() {
        return user;
    }

    public BoardPlace getBoardPlace() {
        return boardPlace;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }
    public SavedBoardPlace(){}
    public SavedBoardPlace(User user, BoardPlace boardPlace, LocalDateTime regDate) {
        this.user = user;
        this.boardPlace = boardPlace;
        this.regDate = regDate;
    }
}
