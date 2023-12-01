package com.traveler.core.dto;

import lombok.Builder;

import java.util.Map;

public class FCMNotificationRequestDTO {
    private String targetUserId;
    private String title;
    private String body;
    private Map<String,String> data;
    // private String image;

    @Builder
    public FCMNotificationRequestDTO(String userId, String title, String body, Map<String, String> data){
        this.targetUserId = userId;
        this.title = title;
        this.body = body;
        this.data = data;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getData() {
        return data;
    }
}
