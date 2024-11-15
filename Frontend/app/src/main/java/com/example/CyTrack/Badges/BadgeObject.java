package com.example.CyTrack.Badges;

/**
 * Represents a Badge object with an id, name, and description.
 */
public class BadgeObject {
    /**
     * The unique identifier for the badge.
     */
    Long id;

    /**
     * The name of the badge.
     */
    String name;

    /**
     * The description of the badge.
     */
    String desc;

    /**
     * Constructs a new BadgeObject with the specified id, title, and info.
     *
     * @param id    the unique identifier for the badge
     * @param title the name of the badge
     * @param info  the description of the badge
     */
    BadgeObject(Long id, String title, String info) {
        this.id = id;
        name = title;
        desc = info;
    }

    /**
     * Returns the name of the badge.
     *
     * @return the name of the badge
     */
    public String getBadgeName() {
        return name;
    }

    /**
     * Sets the name of the badge.
     *
     * @param name the new name of the badge
     */
    public void setBadgeName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the badge.
     *
     * @return the description of the badge
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Sets the description of the badge.
     *
     * @param desc the new description of the badge
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}