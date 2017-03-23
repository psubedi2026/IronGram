package com.theironyard.novauc.entities;

import javax.persistence.*;

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
    User recipient;

    @Column(nullable = false)
    String filename;

    @Column(nullable = false)
    int delay= 10;

    @Column(nullable = false)
    boolean publicPhoto =false;

    public Photo() {
    }

    public Photo(User sender, User recipient, String filename, int delay, boolean publicPhoto) {
        this.sender = sender;
        this.recipient = recipient;
        this.filename = filename;
        this.delay = delay;
        this.publicPhoto = publicPhoto;
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

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isPublicPhoto() {
        return publicPhoto;
    }

    public void setPublicPhoto(boolean publicPhoto) {
        this.publicPhoto = publicPhoto;
    }
}

