package com.traveler.core.entity;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="board_place")
public class BoardPlace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARDPLACE_ID")
    private int boardPlaceId;
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @OneToOne(mappedBy = "boardPlace")
    private Image image;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    public BoardPlace() {

    }
    public Image getImage() {
        return image;
    }

    public int getBoardPlaceId() {
        return boardPlaceId;
    }

    public Board getBoard() {
        return board;
    }

    public Place getPlace() {
        return place;
    }
    public LocalDateTime getRegDate(){
        return regDate;
    }

    @Builder
    public BoardPlace(Board board, Place place, LocalDateTime regDate){
        this.board = board;
        this.place = place;
        this.regDate = regDate;
    }
    public void setImage(Image image){
        this.image = image;
    }
}
