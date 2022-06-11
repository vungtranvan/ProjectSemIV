package com.example.projectsemiv.entity;

public class Question {
    private int id;
    private String name;
    private int categoryExamId;
    private String answer_a;
    private String answer_b;
    private String answer_c;
    private String answer_d;
    private String answer_correct;
    private String image;

    public Question() {
    }

    public Question(String name, int categoryExamId, String answer_a, String answer_b, String answer_c, String answer_d, String answer_correct, String image) {
        this.name = name;
        this.categoryExamId = categoryExamId;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer_d = answer_d;
        this.answer_correct = answer_correct;
        this.image = image;
    }

    public Question(int id, String name, int categoryExamId, String answer_a, String answer_b, String answer_c, String answer_d, String answer_correct, String image) {
        this.id = id;
        this.name = name;
        this.categoryExamId = categoryExamId;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer_d = answer_d;
        this.answer_correct = answer_correct;
        this.image = image;
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

    public int getCategoryExamId() {
        return categoryExamId;
    }

    public void setCategoryExamId(int categoryExamId) {
        this.categoryExamId = categoryExamId;
    }

    public String getAnswer_a() {
        return answer_a;
    }

    public void setAnswer_a(String answer_a) {
        this.answer_a = answer_a;
    }

    public String getAnswer_b() {
        return answer_b;
    }

    public void setAnswer_b(String answer_b) {
        this.answer_b = answer_b;
    }

    public String getAnswer_c() {
        return answer_c;
    }

    public void setAnswer_c(String answer_c) {
        this.answer_c = answer_c;
    }

    public String getAnswer_d() {
        return answer_d;
    }

    public void setAnswer_d(String answer_d) {
        this.answer_d = answer_d;
    }

    public String getAnswer_correct() {
        return answer_correct;
    }

    public void setAnswer_correct(String answer_correct) {
        this.answer_correct = answer_correct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
