package com.example.projectsemiv.entity;

import java.io.Serializable;
import java.util.List;

public class HistoryUpdateVm implements Serializable {
    private int id;
    private int correctMark;
    private int totalMark;
    private List<QuestionHistoryVm> items;

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

    public List<QuestionHistoryVm> getItems() {
        return items;
    }

    public void setItems(List<QuestionHistoryVm> items) {
        this.items = items;
    }

}
