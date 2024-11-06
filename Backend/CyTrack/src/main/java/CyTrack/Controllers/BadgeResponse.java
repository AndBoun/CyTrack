package CyTrack.Controllers;

import java.util.List;

public class BadgeResponse {
    private String status;
    private Data data;
    private String message;

    public BadgeResponse(String status, List<BadgeData> badges, String message) {
        this.status = status;
        this.data = new Data(badges);
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<BadgeData> badges;

        public Data(List<BadgeData> badges) {
            this.badges = badges;
        }

        public List<BadgeData> getBadges() {
            return badges;
        }

        public void setBadges(List<BadgeData> badges) {
            this.badges = badges;
        }
    }

    public static class BadgeData {
        private Long badgeID;
        private String badgeName;
        private String description;

        public BadgeData(Long badgeID, String badgeName, String description) {
            this.badgeID = badgeID;
            this.badgeName = badgeName;
            this.description = description;
        }

        public Long getBadgeID() {
            return badgeID;
        }

        public void setBadgeID(Long badgeID) {
            this.badgeID = badgeID;
        }

        public String getBadgeName() {
            return badgeName;
        }

        public void setBadgeName(String badgeName) {
            this.badgeName = badgeName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
