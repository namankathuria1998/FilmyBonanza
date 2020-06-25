package src.com.filmybonanza.handler;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.model.BookedEvent;
import src.com.filmybonanza.dao.DynamodbImpl;
import src.com.filmybonanza.R;
import src.com.filmybonanza.model.UserDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import java.util.ArrayList;

public class UserHandler extends AppCompatActivity {

    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_handler);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public UserDetails getUserDetails(String uid,Context context)
    {
        // this method will accept a userId and then display
        //  the user' details on the screen / activity

        UserDetails userDetails = DynamodbImpl.dynamodbDao.getUserDetails
                (uid,context);
        return userDetails;
    }


    public boolean updateUserDetails(String uid,UserDetails newUserDetails)
    {
        // this method will accept new Userdetails and a userId
        // and then update the old details of the user having this userId
        // with the new-ones
        return DynamodbImpl.dynamodbDao.updateUserDetails(uid,newUserDetails);
    }

    public ArrayList<BookedEvent> getUserBookings(String userId)
    {
        return DynamodbImpl.dynamodbDao.getUserBookings(userId);
    }

    public void bookMovie(BookedEvent bookedEvent)
    {
        DynamodbImpl.dynamodbDao.bookMovie(bookedEvent);
    }

    public void putNewUserintoDatabase(String id,String name,String email)
    {
        DynamodbImpl.dynamodbDao.putNewUserintoDatabase(id,name,email);
    }
}
