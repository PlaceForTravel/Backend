package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.BoardPlace;
import com.traveler.core.entity.Image;
import com.traveler.core.entity.Place;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlaceRequestDTO {
    private int placeId;
    private String placeName;
    private float latitude;
    private float longitude;
    private String address;
    private int imgIndex;
    private String category;



    public String getPlaceName(){
        return placeName;
    }

    public PlaceRequestDTO(){}


    public PlaceRequestDTO(String placeName, float latitude, float longitude, String address,String category) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.category = category;

    }

    public Place toPlaceEntity(){
        return Place.builder().address(address).name(placeName).longitude(longitude).latitude(latitude).category(category).build();
    }

    public Image toImageEntity(String imgUrl, BoardPlace boardPlace){
        return Image.builder().fileName(imgUrl).boardPlace(boardPlace).build();
    }


    public int getImgIndex() {
        return imgIndex;
    }
}
