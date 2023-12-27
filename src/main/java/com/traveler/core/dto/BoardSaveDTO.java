package com.traveler.core.dto;

import com.traveler.core.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardSaveDTO {

    private String content;
    private String cityName;
    private List<PlaceRequestDTO> places;
    private String userId;
    private String nickname;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setPlaces(List<PlaceRequestDTO> places) {
        this.places = places;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<PlaceRequestDTO> getPlaces() {
        return places;
    }
    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }


    public Board toBoardEntity(User user){
        return Board.builder().content(content).cityName(cityName).user(user).build();
    }

}
