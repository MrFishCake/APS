package com.example.testfx.DateBase;

import java.util.Date;

public class Animal {
    public static String owner;
    public static int id_own;
    private String nickname;
    private String kind;
    private String gender;
    private Date date_of_birth;

    public Animal() {}
    public Animal(String nickname, String kind, String gender, Date date_of_birth) {

        this.nickname = nickname;
        this.kind = kind;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getKind() {
        return kind;
    }

    public String getGender() {
        return gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }
}
