package com.traveler.core.dto;

public class SavedBoardPlaceRequestDTO {
    public String getUserId() {
        return userId;
    }

    private String userId;
    private String cityName;

    public String getCityName() {
        return cityName;
    }
}
