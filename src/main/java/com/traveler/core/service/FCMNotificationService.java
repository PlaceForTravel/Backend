package com.traveler.core.service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.traveler.core.dto.FCMNotificationRequestDTO;
import com.traveler.core.entity.User;
import com.traveler.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;


    @Autowired
    public FCMNotificationService(FirebaseMessaging firebaseMessaging, UserRepository userRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.userRepository = userRepository;
    }
    public String sendNotificationByToken(FCMNotificationRequestDTO fcmNotificationRequestDTO) {
        Optional<User> user = userRepository.findById(fcmNotificationRequestDTO.getTargetUserId());

        if (user.isPresent()) {
            if (user.get().getFcmToken() != null) { //get()으로 하는 이유
                Notification notification = Notification.builder()
                        .setBody(fcmNotificationRequestDTO.getBody())
                        .setTitle(fcmNotificationRequestDTO.getTitle())
                        .build();
                Message message = Message.builder()
                        .setNotification(notification)
                        .setToken(user.get().getFcmToken())
                        .putAllData(fcmNotificationRequestDTO.getData())
                        .build();
                try {
                    firebaseMessaging.send(message);
                    return "알림 전송 성공 targetUserId=" + fcmNotificationRequestDTO.getTargetUserId();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace(); //예외의 정보를 콘솔에 출력
                    return "알림 전송 실패 targetUserId=" + fcmNotificationRequestDTO.getTargetUserId();
                }
            } else {
                return "서버에 저장된 해당 유저의 fcm token이 존재하지 않습니다. targetUserId = " + fcmNotificationRequestDTO.getTargetUserId();
            }
        } else {
            return "해당 유저가 존재 하지 않습니다. targetUserId = "+ fcmNotificationRequestDTO.getTargetUserId();
        }



    }


}
