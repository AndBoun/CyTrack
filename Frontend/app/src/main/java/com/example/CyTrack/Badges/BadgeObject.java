package com.example.CyTrack.Badges;

public class BadgeObject {
    Long id;
    String name;
    String desc;

    BadgeObject(Long id, String title, String info) {
        this.id = id;
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
