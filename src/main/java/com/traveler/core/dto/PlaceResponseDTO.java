package com.traveler.core.dto;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Place;

public class PlaceResponseDTO {
    private int placeId;
    private String placeName;
    private float latitude;
    private float longitude;
    private String address;
    private int boardPlaceid;
    private String imgUrl;
    public PlaceResponseDTO(Place place, BoardPlace boardPlace) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.address = place.getAddress();
        this.boardPlaceid = boardPlace.getBoardPlaceId();
        this.imgUrl = boardPlace.getImage().getFileName();
    }

}
