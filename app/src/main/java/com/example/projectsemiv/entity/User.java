package com.example.projectsemiv.entity;

import java.util.Date;

public class User {
    private int id;
    private String userName;
    private String password;
    private String name;
    private String email;
    private String image;
    private Date birthday;
    private String address;
    private String phone;
    private boolean sex;
    private boolean isAdmin;

    public User() {
    }

    public User(String userName, String password, String name, String email, String image, Date birthday, String address, String phone, boolean sex, boolean isAdmin) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.image = image;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
        this.sex = sex;
        this.isAdmin = isAdmin;
    }

    public User(int id, String userName, String password, String name, String email, String image, Date birthday, String address, String phone, boolean sex, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.image = image;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
