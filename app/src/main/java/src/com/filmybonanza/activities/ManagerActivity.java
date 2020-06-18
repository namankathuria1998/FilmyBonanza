package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

      // adding a new event in the Events Database
      DependencyInjection.getEventHandler().addEvent(new Event("Radhe","A hindi drama and action film",
              "https://cdn.kalingatv.com/wp-content/uploads/2020/03/breaking-radhe-koimoi.jpg","1245","Movie","25 June 2020"));

    }
}
