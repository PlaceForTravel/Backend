package com.traveler.core.entity;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name="image")
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private int imageId;
    @OneToOne
    @JoinColumn(name = "BOARDPLACE_ID")
    private BoardPlace boardPlace;
    @Column(name = "FILE_NAME")
    private String fileName; //url

    public int getImageId() {
        return imageId;
    }

    public BoardPlace getBoardPlace() {
        return boardPlace;
    }

    public String getFileName() {
        return fileName;
    }
    public Image(){}
    @Builder
    public Image(BoardPlace boardPlace, String fileName){
        this.boardPlace = boardPlace;
        this.fileName = fileName;
    }
}
