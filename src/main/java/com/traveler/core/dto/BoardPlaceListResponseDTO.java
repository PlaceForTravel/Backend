package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.BoardPlace;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BoardPlaceListResponseDTO {
    private String imgUrl ;
    private String placeName;
    private String cityname;
    private String address;
    private int likeCount;
    private String userId;
    private int boardId;
    private String nickname;
    private LocalDateTime boardRegDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime deletedDate;
    private String content;

    public BoardPlaceListResponseDTO(BoardPlace entity){
        this.boardId = entity.getBoard().getBoardId();
        this.likeCount = entity.getBoard().getLikeCount();
        this.userId = entity.getBoard().getUser().getUserId();
        this.nickname = entity.getBoard().getUser().getNickname();
        this.imgUrl = entity.getImage().getFileName();
        this.cityname = entity.getPlace().getPlaceName();
        this.address = entity.getPlace().getAddress();
        this.boardRegDate = entity.getBoard().getRegDate();
        this.modifiedDate = entity.getBoard().getModifiedDate();
        this.deletedDate = entity.getBoard().getDeletedDate();
        this.content = entity.getBoard().getContent();
    }
}
