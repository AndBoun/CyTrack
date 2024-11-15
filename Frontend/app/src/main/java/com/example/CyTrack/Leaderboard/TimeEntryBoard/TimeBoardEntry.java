package com.example.CyTrack.Leaderboard.TimeEntryBoard;

/**
 * Represents an entry in the TimeBoard leaderboard.
 */
public class TimeBoardEntry {
    /**
     * The unique identifier for the entry.
     */
    Long id;

    /**
     * The name associated with the entry.
     */
    String name;

    /**
     * The time value for the entry.
     */
    int time;

    /**
     * Constructs a new TimeBoardEntry.
     *
     * @param id    the unique identifier for the entry
     * @param title the name associated with the entry
     * @param time  the time value for the entry
     */
    TimeBoardEntry(Long id, String title, int time) {
        this.id = id;
        name = title;
        this.time = time;
    }

    /**
     * Gets the name associated with the entry.
     *
     * @return the name of the entry
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with the entry.
     *
     * @param name the new name for the entry
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the time value of the entry.
     *
     * @return the time value of the entry
     */
    public int getTime() {
        return this.time;
    }

    /**
     * Sets the time value of the entry.
     *
     * @param entry the new time value for the entry
     */
    public void setTime(int entry) {
        time = entry;
    }
}