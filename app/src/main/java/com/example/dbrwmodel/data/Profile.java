package com.example.dbrwmodel.data;

public class Profile {
    public long _id;
    public String sName, name;
    public int age, photo;

    public Profile(){}

    public Profile(String sName, String name, int age, int photo) {
        this._id = _id;
        this.sName = sName;
        this.name = name;
        this.age = age;
        this.photo = photo;
    }
    public Profile(long _id, String sName, String name, int age, int photo) {
        this._id = _id;
        this.sName = sName;
        this.name = name;
        this.age = age;
        this.photo = photo;
    }
}
