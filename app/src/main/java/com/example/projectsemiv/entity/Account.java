package com.example.projectsemiv.entity;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private int id;
    private String userName;
    private String password;
    private String name;
    private String email;
    private String image;
    private String address;
    private boolean sex;
    private boolean isAdmin;

    public Account() {
    }

    public Account(int id) {
        this.id = id;
    }

    public Account(String userName, String password, String name, String email, String image, String address, boolean sex, boolean isAdmin) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.image = image;
        this.address = address;
        this.sex = sex;
        this.isAdmin = isAdmin;
    }

    public Account(int id, String userName, String password, String name, String email, String image, String address, boolean sex, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.image = image;
        this.address = address;
        this.sex = sex;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
