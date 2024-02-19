package com.traveler.core.dto;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Place;

public class PlaceResponseDTO {
    public int placeId;
    public  String placeName;
    public  float latitude;
    public  float longitude;
    public  String address;
    public  int boardPlaceid;
    public  String imgUrl;
    public  String category;
    public  boolean like;

    public PlaceResponseDTO() {
    }
    public PlaceResponseDTO(Place place, BoardPlace boardPlace, boolean like) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.address = place.getAddress();
        this.boardPlaceid = boardPlace.getBoardPlaceId();
        this.imgUrl = boardPlace.getImage().getFileName();
        this.category = place.getCategory();
        this.like = like;
    }

}
