package src.com.filmybonanza.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;

public class Ticket extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);

        final Intent intent=getIntent();
        String json = intent.getStringExtra("key");
        String fee = intent.getStringExtra("fee");
        BookedEvent bookedEvent  =  DependencyInjection.getGson().fromJson(json, BookedEvent.class);

        ImageView iv = findViewById(R.id.image);
        Picasso.get().load(bookedEvent.getPoster()).into(iv);
        TextView title=findViewById(R.id.title);
        title.setText(bookedEvent.getTitle());
        TextView evetid = findViewById(R.id.id);
        evetid.setText("Event Id: "+bookedEvent.getEventId());
        TextView date = findViewById(R.id.date);
        date.setText("Event Date: "+bookedEvent.getDate());
        TextView timings = findViewById(R.id.timings);
        timings.setText("Show Timings: "+bookedEvent.getTimings());
        TextView location = findViewById(R.id.location);
        location.setText("Location of the event: "+bookedEvent.getLocation());
        TextView totalfee = findViewById(R.id.totalfee);
        totalfee.setText("Total Fee: "+ fee);


    }

}
