package com.example.projectsemiv.entity;

import java.util.Date;

public class Question {

    private Integer id;
    private String name;
    private String answera;
    private String answerb;
    private String answerc;
    private String answerd;
    private String answerCorrect;
    private String image;
    private Date createdDate;
    private Date updatedDate;

    public Question() {
    }

    public Question(String name, String answera, String answerb, String answerc, String answerd, String answerCorrect, String image) {
        this.name = name;
        this.answera = answera;
        this.answerb = answerb;
        this.answerc = answerc;
        this.answerd = answerd;
        this.answerCorrect = answerCorrect;
        this.image = image;
    }

    public Question(Integer id, String name, String answera, String answerb, String answerc, String answerd, String answerCorrect, String image) {
        this.id = id;
        this.name = name;
        this.answera = answera;
        this.answerb = answerb;
        this.answerc = answerc;
        this.answerd = answerd;
        this.answerCorrect = answerCorrect;
        this.image = image;
    }

    public Question(Integer id, String name, String answera, String answerb, String answerc, String answerd, String answerCorrect, String image, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.answera = answera;
        this.answerb = answerb;
        this.answerc = answerc;
        this.answerd = answerd;
        this.answerCorrect = answerCorrect;
        this.image = image;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswera() {
        return answera;
    }

    public void setAnswera(String answera) {
        this.answera = answera;
    }

    public String getAnswerb() {
        return answerb;
    }

    public void setAnswerb(String answerb) {
        this.answerb = answerb;
    }

    public String getAnswerc() {
        return answerc;
    }

    public void setAnswerc(String answerc) {
        this.answerc = answerc;
    }

    public String getAnswerd() {
        return answerd;
    }

    public void setAnswerd(String answerd) {
        this.answerd = answerd;
    }

    public String getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(String answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
