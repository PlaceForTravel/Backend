package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Image;
import com.traveler.core.entity.Place;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlaceRequestDTO {
    private int placeId;
    private String placeName;
    private float latitude;
    private float longitude;
    private String address;
    private int imgIndex;
    private int boardPlaceid;
    public String getPlaceName(){
        return placeName;
    }
    public int getImgIndex(){
        return imgIndex;
    }
    public PlaceRequestDTO(){}


    public PlaceRequestDTO(String placeName, float latitude, float longitude, String address, int imgIndex) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.imgIndex = imgIndex;
    }

    public Place toPlaceEntity(){
        return Place.builder().address(address).name(placeName).longitude(longitude).latitude(latitude).build();
    }

    public Image toImageEntity(String imgUrl, BoardPlace boardPlace){
        return Image.builder().fileName(imgUrl).boardPlace(boardPlace).build();
    }

}
