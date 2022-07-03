package com.example.projectsemiv.entity;

import java.io.Serializable;

public class QuestionVm implements Serializable {
    private int id;
    private String name;
    private String categoryExamName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryExamName() {
        return categoryExamName;
    }

    public void setCategoryExamName(String categoryExamName) {
        this.categoryExamName = categoryExamName;
    }
}
