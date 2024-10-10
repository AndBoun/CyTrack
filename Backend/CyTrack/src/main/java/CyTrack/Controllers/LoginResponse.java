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
    public LoginResponse(String status, Long userID, String firstName, String lastName, int age, String gender, int streak, String message) {
        this.status = status;
        this.data = new Data(userID, firstName, lastName, age, gender, streak);
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
        private String firstName;
        private String lastName;
        private int age;
        private String gender;
        private int streak;

        // Constructor for login response
        public Data(Long userID) {
            this.userID = userID;
        }

        // Constructor for GET response
        public Data(Long userID, String firstName, String lastName, int age, String gender, int streak) {
            this.userID = userID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
            this.streak = streak;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
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
    }
}