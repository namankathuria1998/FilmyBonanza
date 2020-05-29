package src.com.filmybonanza.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;


// this activity / screen will display the details of a particular movie
public class ShowEventDetails extends AppCompatActivity {


    TextView title,overview,date;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Intent intent=getIntent();
        String json = intent.getStringExtra("key");
        Gson gson=new Gson();
        final Event event = gson.fromJson(json,Event.class);

//        final EventActivity eventActivity=new EventActivity();
        DependencyInjection.getEventHandler().displayEventDetails(event,this);

        FloatingActionButton fabsave=findViewById(R.id.fab);
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
