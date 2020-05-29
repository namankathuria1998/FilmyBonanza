package src.com.filmybonanza;


public class UserDetails {

    private String userName , userEmail , UserPassword , uid;

    public UserDetails(String userName, String userEmail, String UserPassword, String uid) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.UserPassword = UserPassword;
        this.uid = uid;
    }

    public UserDetails() {
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserPassword() {
        return this.UserPassword;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

