package src.com.filmybonanza.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.R;
import src.com.filmybonanza.model.UserDetails;

// User Sign-up and login will be done in this activity / screen
public class MainActivity extends AppCompatActivity {

    Intent loginintent;
    Context context;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void addUser(UserDetails userDetails) {
        // This method will parse all the details of a user
        // and then register the user. This user will also be added to the Users' database
    }

    public FirebaseUser loginUser(FirebaseUser firebaseUser) {

        // This method will take in the user details and then first authenticate the user.
        // If the details are invalid , then user will be asked to enter the valid details
        // Upon authentication , the user will be logged-in into the app .
        // It will then authorize the user to check whether he can enter as an admin or not

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Enter app as an Admin ??")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent inte = new Intent(MainActivity.this, UserActivity.class);
                        MainActivity.this.startActivity(inte);
                        finish();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals("XPluEZYWjacVuRRcwzinIHt9K212")) {
                            Intent inte = new Intent(MainActivity.this, ManagerActivity.class);
                            MainActivity.this.startActivity(inte);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Dont have the Required permission , entering as a normal user",Toast.LENGTH_LONG).show();
                        Intent inte = new Intent(MainActivity.this, UserActivity.class);

                        MainActivity.this.startActivity(inte);
                        finish();
                        }
                    }
                })
                .create();

        if (firebaseUser == null) {
            Intent loginintent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.mipmap.mymovie)
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

        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.e("TAG", "onStart: called");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loginUser(firebaseUser);
    }
}

