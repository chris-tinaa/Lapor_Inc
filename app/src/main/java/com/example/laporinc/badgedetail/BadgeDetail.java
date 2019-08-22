package com.example.laporinc.badgedetail;

public class BadgeDetail {

    private String badgeName;
    private int progress;

    public BadgeDetail() {
    }

    public BadgeDetail(String badgeName, int progress) {
        this.badgeName = badgeName;
        this.progress = progress;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
