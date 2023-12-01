package com.traveler.core.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "NICKNAME")
    private String nickname;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE_NUM")
    private String phoneNum;
    @Column(name = "FCMTOKEN")
    private String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SavedBoard> savedBoardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SavedBoardPlace> savedBoardPlaceList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public List<Board> getBoardList() {
        return boardList;
    }

    public List<SavedBoard> getSavedBoardList() {
        return savedBoardList;
    }

    public List<SavedBoardPlace> getSavedBoardPlaceList() {
        return savedBoardPlaceList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }


}

