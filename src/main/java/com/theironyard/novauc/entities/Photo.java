package com.theironyard.novauc.entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by psubedi2020 on 3/21/17.
 */
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    @Column(nullable = false)
    String filename;

    int timer;

    String isPublic;

    public Photo() {
    }

    public Photo(int id, User sender, User receiver, String filename, int timer, String isPublic) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.filename = filename;
        this.timer = timer;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}

