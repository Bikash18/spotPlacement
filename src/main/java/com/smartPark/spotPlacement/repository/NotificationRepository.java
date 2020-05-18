package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    public List<Notification> findByTitle(String title);
    public List<Notification> findByNotificationSubType(String notificationSubType);
}