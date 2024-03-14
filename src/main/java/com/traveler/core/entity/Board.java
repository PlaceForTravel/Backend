package com.traveler.core.entity;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Table(name="board")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private int boardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "LIKE_COUNT")
    private int likeCount;
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;
    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate;
    @Column(name = "CITY_NAME")
    private String cityName;


    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY)
    private List<BoardPlace> boardPlaceList = new ArrayList<>();

    public Board(int boardId, User user, String content, int likeCount, LocalDateTime regDate, LocalDateTime modifiedDate, LocalDateTime deletedDate, String cityName, List<BoardPlace> boardPlaceList) {
        this.boardId = boardId;
        this.user = user;
        this.content = content;
        this.likeCount = likeCount;
        this.regDate = regDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
        this.cityName = cityName;
        this.boardPlaceList = boardPlaceList;
    }

    public Board() {
    }

    public int getBoardId() {
        return boardId;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public LocalDateTime getModifiedDate() {

        return modifiedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public String getCityName() {
        return cityName;
    }


    public List<BoardPlace> getBoardPlaceList() {
        return boardPlaceList;
    }

    @Builder
    public Board(String content, String cityName,User user){
        this.content = content;
        this.regDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        this.cityName = cityName;
        this.user = user;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate){
        this.modifiedDate = modifiedDate;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setLikeCount(int count){
        likeCount = count;
    }
}