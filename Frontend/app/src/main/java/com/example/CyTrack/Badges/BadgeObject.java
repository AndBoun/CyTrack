package com.example.CyTrack.Badges;

public class BadgeObject {
    String name;
    String desc;

    BadgeObject(String title, String info) {
        name = title;
        desc = info;

    }

    public String getBadgeName() {
        return name;
    }

    public void setBadgeName(String name) {
        this.name = name
        ;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
