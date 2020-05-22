package com.example.filmybonanza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.filmybonanza.model.UserDetails;

// the UserDetails will be displayed on this screen/activity
public class ShowUserDetails extends AppCompatActivity {


    Context context;
    public ShowUserDetails(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_details);
    }

    void displayUserDetails(String userId)
    {
        // this method will accept a userId and then display
        //  the user' details on the screen / activity

        Toast.makeText(context,"This will display user details",Toast.LENGTH_LONG).show();
    }

    void updateUserDetails(String userId , UserDetails newUserDetails)
    {
        // this method will accept new Userdetails and a userId
        // and then update the old details of the user having this userId
        // with the new-ones
    }


}
