package com.example.projectsemiv.entity;

import java.io.Serializable;

public class HistoryVm implements Serializable {

    private int id;
    private String accountName;
    private String categoryExamName;
    private int correctMark;
    private int totalMark;
    private boolean status;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCategoryExamName() {
        return categoryExamName;
    }

    public void setCategoryExamName(String categoryExamName) {
        this.categoryExamName = categoryExamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorrectMark() {
        return correctMark;
    }

    public void setCorrectMark(int correctMark) {
        this.correctMark = correctMark;
    }

    public int getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}