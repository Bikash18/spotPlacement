package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Notification represents all the notification msg
 * @author Bikash
 * @version 1.0
 */
@Document(collection = "notification")
public class Notification {
    @Id
    private String id;

    private String message;

    private String notificationType;

    private String notificationSubType;

    private int priority;

    private String title;

    private long date;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getNotificationSubType() {
        return notificationSubType;
    }

    public int getPriority() {
        return priority;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setNotificationSubType(String notificationSubType) {
        this.notificationSubType = notificationSubType;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Notification(String message, String notificationType, String notificationSubType, int priority, String title, long date) {
        this.message = message;
        this.notificationType = notificationType;
        this.notificationSubType = notificationSubType;
        this.priority = priority;
        this.title = title;
        this.date = date;
    }
}