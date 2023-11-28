package com.traveler.core.dto;

import com.traveler.core.entity.User;

public class CommentRequestDTO {

    private String content;
    private String userId;
    private String nickname;

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }
}
