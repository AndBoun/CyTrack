package CyTrack.Controllers;

public class LoginResponse {
    private String status;
    private Data data;
    private String message;

    // Constructor for login response
    public LoginResponse(String status, Long userID, String message) {
        this.status = status;
        this.data = new Data(userID);
        this.message = message;
    }

    // Constructor for GET response
    public LoginResponse(String status, Long userID,String username, String firstName, String lastName, int age, int height, double weight, String gender, int streak, String imageurl, String message) {
        this.status = status;
        this.data = new Data(userID, username, firstName, lastName, age, height, weight, gender, streak, imageurl);
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
        private Long userID;
        private String username;
        private String firstName;
        private String lastName;
        private int age;
        private int height;
        private double weight;
        private String gender;
        private int streak;
        private String imageurl;

        // Constructor for login response
        public Data(Long userID) {
            this.userID = userID;
        }

        // Constructor for GET response
        public Data(Long userID,String username, String firstName, String lastName, int age, int height, double weight, String gender, int streak, String imageurl) {
            this.userID = userID;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.gender = gender;
            this.streak = streak;
            this.imageurl = imageurl;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

        public String getUsername() {return username; }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getHeight() {return height; }

        public void setHeight(int height) {this.height = height; }

        public double getWeight() {return weight; }

        public void setWeight(double weight) {this.weight = weight; }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getStreak() {
            return streak;
        }

        public void setStreak(int streak) {
            this.streak = streak;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}