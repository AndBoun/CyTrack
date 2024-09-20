package com.coms309.helloworld.entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    public Message(){

    }

    public Message(String content){
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
