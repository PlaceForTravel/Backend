package com.traveler.core.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.traveler.core.entity.Board;
import com.traveler.core.entity.BoardPlace;

import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BoardDetailResponseDTO {
    private int boardId;
    private int likeCount;
    private String userId;
    private String nickname;
    private String content;
    private String regDate;
    private String modifiedDate;
    private String deletedDate;
    private String cityName;
    private List<PlaceResponseDTO> placeResponseDTOS;

    public BoardDetailResponseDTO(Board board, List<BoardPlace> boardPlaces){
        this.boardId = board.getBoardId();
        this.likeCount = board.getLikeCount();
        this.userId = board.getUser().getUserId();
        this.nickname = board.getUser().getNickname();
        this.content = board.getContent();
        this.regDate = board.getRegDate().toString();
        this.cityName = board.getCityName();
        this.deletedDate = board.getDeletedDate()==null ? null : board.getDeletedDate().toString();
        this.modifiedDate = board.getModifiedDate()==null ? null : board.getModifiedDate().toString();
        this.placeResponseDTOS = boardPlaces.stream().map(boardPlace -> new PlaceResponseDTO(boardPlace.getPlace(),boardPlace)).collect(Collectors.toList());
    }
}
