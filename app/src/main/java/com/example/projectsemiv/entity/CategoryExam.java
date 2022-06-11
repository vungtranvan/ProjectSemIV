package com.example.projectsemiv.entity;

public class CategoryExam {
    private int id;
    private String name;

    public CategoryExam(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryExam() {
    }

    public CategoryExam(String name) {
        this.name = name;
    }

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
}
