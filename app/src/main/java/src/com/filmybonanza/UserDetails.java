package src.com.filmybonanza;


public class UserDetails {

    private String userName , userEmail , UserPhoneNo , uid;

    public UserDetails(String userName, String userEmail, String UserPhoneNo, String uid) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.UserPhoneNo = UserPhoneNo;
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

    public String getUserPhoneNo() {
        return this.UserPhoneNo;
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

    public void setUserPhoneNo(String UserPhoneNo) {
        this.UserPhoneNo = UserPhoneNo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

