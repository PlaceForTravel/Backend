package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.Board;
import com.traveler.core.entity.BoardPlace;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BoardListResponseDTO {
    private int boardId;
    private String userId;
    private int likeCount;
    private String nickname;
    private String regDate;
    private String modifiedDate;
    private String deletedDate;
    private String cityName;
    private List<String> imgUrl;
    private boolean like;




    public BoardListResponseDTO(Board entity, boolean like){
        this.boardId = entity.getBoardId();
        this.userId = entity.getUser().getUserId();
        this.likeCount = entity.getLikeCount();
        this.regDate = entity.getRegDate().toString();
        this.deletedDate = entity.getDeletedDate()==null ? null : entity.getDeletedDate().toString();
        this.modifiedDate = entity.getModifiedDate()==null ? null : entity.getModifiedDate().toString();
        this.cityName = entity.getCityName();
        this.nickname = entity.getUser().getNickname();
        List<BoardPlace> boardPlaceList = entity.getBoardPlaceList();
        List<String> imageUrls = new ArrayList<>();
        for (BoardPlace boardPlace : boardPlaceList) {
            imageUrls.add(boardPlace.getImage().getFileName());
        }
        this.imgUrl = imageUrls;
        this.like = like;
    }
}
