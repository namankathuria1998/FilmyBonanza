package com.example.filmybonanza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.example.filmybonanza.model.UserDetails;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;


// User Sign-up and login will be done in this activity / screen
public class MainActivity extends AppCompatActivity {

    Intent loginintent;
    Context context;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
    }


    public void addUser(UserDetails userDetails) {
        // This method will parse all the details of a user
        // and then register the user. This user will also be added to the Users' database
    }

    public void loginUser(FirebaseUser firebaseUser) {

        // This method will take in the user details and then first authenticate the user.
        // If the details are invalid , then user will be asked to enter the valid details
        // Upon authentication , the user will be logged-in into the app .
        // It will then authorize the user to check whether he can enter as an admin or not

        if (firebaseUser == null) {
            loginintent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)

                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build())
                    )
                    .build();
            startActivity(loginintent);

        }
        else {

            ProgressDialog.show(this, "Loading", "Wait while loading...");
            alertDialog.show();
        }

    }

    // A simple function to connect different buttons with their respective method
//    public void myfunction(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.details:
//                ShowUserDetails showUserDetails=new ShowUserDetails(this);
//                showUserDetails.displayUserDetails("userid");
//                break;
//
//            case R.id.history:
//                MovieBookings movieBookings=new MovieBookings(this);
//                movieBookings.displayUserBookings("userid");
//                break;
//
//            case R.id.movies:
//                UpcomingMovies upcomingMovies=new UpcomingMovies(this);
//                upcomingMovies.displayMovies();
//                break;
//
//        }
//
//    }


    @Override
    protected void onStart() {

        super.onStart();
        Log.e("TAG", "onStart: called");
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("Enter app as a manager ??")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent inte = new Intent(MainActivity.this, UserDetails.class);
                        context.startActivity(inte);
                        finish();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals("dWvvk1KXtNgooF4Bmqs6Bwxr2ro2")) {
                            Intent inte = new Intent(MainActivity.this, ManagerActivity.class);
                            context.startActivity(inte);
                            finish();
                        } else {
                            Intent inte = new Intent(MainActivity.this, UserDetails.class);

                            context.startActivity(inte);
                            finish();
                        }
                    }
                })
                .create();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loginUser(firebaseUser);

//        AmazonDynamoDB client =  AmazonDynamoDBClientB
//        CreateTableRequest request;
//        List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
//        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
//        DynamoDB dynamoDB=new DynamoDB(client);
//        Item




    }
}

