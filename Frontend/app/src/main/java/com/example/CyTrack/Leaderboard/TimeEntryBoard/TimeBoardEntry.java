package com.example.CyTrack.Leaderboard.TimeEntryBoard;

public class TimeBoardEntry {
    Long id;
    String name;
    int time;

    TimeBoardEntry(Long id, String title, int time) {
        this.id = id;
        name = title;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name
        ;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int entry) {
        time = entry;
    }
}
