package com.traveler.core.dto;

import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Place;

public class PlaceResponseDTO {
    private final int placeId;
    private final String placeName;
    private final float latitude;
    private final float longitude;
    private final String address;
    private final int boardPlaceid;
    private final String imgUrl;
    private final String category;
    public PlaceResponseDTO(Place place, BoardPlace boardPlace) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.address = place.getAddress();
        this.boardPlaceid = boardPlace.getBoardPlaceId();
        this.imgUrl = boardPlace.getImage().getFileName();
        this.category = place.getCategory();
    }

}
