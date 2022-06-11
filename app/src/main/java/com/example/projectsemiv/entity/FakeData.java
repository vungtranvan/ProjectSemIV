package com.example.projectsemiv.entity;

import java.util.ArrayList;
import java.util.List;

public class FakeData {

    public List<Question01> getQuestionFake() {
        List<Question01> lstData = new ArrayList<Question01>();
        lstData.add(new Question01(1, "1 + 1 = ?", "1", "2", "3", "4", "B", 1, "Toan", "img_1", ""));
        lstData.add(new Question01(2, "1 + 2 = ?", "1", "2", "3", "4", "C", 1, "Toan", "", ""));
        lstData.add(new Question01(3, "2 + 2 = ?", "1", "2", "3", "4", "D", 1, "Toan", "img_2", ""));
        lstData.add(new Question01(4, "1 x 1 = ?", "1", "2", "3", "4", "A", 1, "Toan", "", ""));
        lstData.add(new Question01(5, "1 + 3 = ?", "1", "2", "3", "4", "D", 1, "Toan", "", ""));
        lstData.add(new Question01(6, "5 + 1 = ?", "4", "6", "7", "8", "B", 1, "Toan", "", ""));
        lstData.add(new Question01(7, "6 - 1 = ?", "1", "2", "5", "4", "C", 1, "Toan", "", ""));
        lstData.add(new Question01(8, "1 + 5 = ?", "1", "6", "3", "4", "B", 1, "Toan", "", ""));
        lstData.add(new Question01(9, "1 + 7 = ?", "8", "2", "3", "4", "A", 1, "Toan", "", ""));
        lstData.add(new Question01(10, "1 + 4 = ?", "1", "2", "5", "2", "C", 1, "Toan", "", ""));
        lstData.add(new Question01(11, "2 x 3 = ?", "6", "2", "80", "4", "A", 1, "Toan", "", ""));
        lstData.add(new Question01(12, "10 + 1 = ?", "11", "21", "30", "45", "A", 1, "Toan", "", ""));
        lstData.add(new Question01(13, "10 + 10 = ?", "10", "20", "30", "40", "B", 1, "Toan", "", ""));
        lstData.add(new Question01(14, "0 x 10 = ?", "1", "2", "3", "0", "D", 1, "Toan", "", ""));
        lstData.add(new Question01(15, "1 + 9 = ?", "10", "20", "30", "40", "A", 1, "Toan", "", ""));
        return lstData;
    }

    public List<CategoryExam> getExamFake() {
        List<CategoryExam> lstData = new ArrayList<CategoryExam>();
        lstData.add(new CategoryExam("Đề số 1"));
        lstData.add(new CategoryExam("Đề số 2"));
        lstData.add(new CategoryExam("Đề số 3"));
        lstData.add(new CategoryExam("Đề số 4"));
        lstData.add(new CategoryExam("Đề số 5"));
        lstData.add(new CategoryExam("Đề số 6"));
        return lstData;
    }
}
