package src.com.filmybonanza.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.ManagerAdapter;
import src.com.filmybonanza.R;
import src.com.filmybonanza.dynamodbClient.DynamodbClient;
import src.com.filmybonanza.singleton.DependencyInjection;

public class ManagerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static ManagerAdapter adapter;
    final ArrayList<Event> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        recyclerView = findViewById(R.id.managerrecycle);
        adapter = new ManagerAdapter(arrayList, this );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        LayoutInflater lif=LayoutInflater.from(ManagerActivity.this);
        final View details =lif.inflate(R.layout.enterdetailsofevent,null);
        final EditText title = details.findViewById(R.id.title);
        final EditText date = details.findViewById(R.id.date);
        final EditText poster = details.findViewById(R.id.posteredit);
        final EditText eventId = details.findViewById(R.id.eventid);

        Map<Integer,String>mapping=new HashMap<>();



//        final EditText timings = details.findViewById(R.id.timings);
        final EditText summaryedit = details.findViewById(R.id.summaryedit);

        ArrayList<Event>newarrayList= DependencyInjection.getEventHandler().getEvents("Movie");

        arrayList.clear();
        arrayList.addAll(newarrayList);
        adapter.notifyDataSetChanged();


        final PutItemRequest putItemRequest=new PutItemRequest();
        putItemRequest.setTableName("Events");
        final Map<String,AttributeValue> map=new HashMap<>();


        final AlertDialog detailsalertDialog=new AlertDialog.Builder(this)
                .setView(details)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        map.put("eventId",new AttributeValue(eventId.getText().toString()));
                        map.put("date",new AttributeValue(date.getText().toString()));
                        map.put("title",new AttributeValue(title.getText().toString()));
                        map.put("poster",new AttributeValue(poster.getText().toString()));
//                        map.put("timings",new AttributeValue(timings.getText().toString()));
                        map.put("summary",new AttributeValue(summaryedit.getText().toString()));

                        putItemRequest.setItem(map);
                        DynamodbClient.getClient().putItem(putItemRequest);

                        final View capacityView =lif.inflate(R.layout.capacityfordifflocations,null);


                        final AlertDialog newAlertDialog=new AlertDialog.Builder(ManagerActivity.this)
                                .setView(capacityView)
                                .setPositiveButton("Add Capacity", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        ProgressDialog.show(ManagerActivity.this, "Loading", "Wait while Movie is being Added...");


//                                        final ProgressDialog dialog = new ProgressDialog();
//                                        dialog.setTitle("Wait while Movie is being Added...");
//                                        dialog.setMessage("Please wait.");
//                                        dialog.setIndeterminate(true);
//                                        dialog.setCancelable(false);
//                                        dialog.show();

                                        mapping.clear();

                                        mapping.put(R.id.rb11 , eventId.getText().toString() + "PVR Vikaspuri , New Delhi" + "  10 AM - 1 PM ");
                                        mapping.put(R.id.rb21 , eventId.getText().toString() + "PVR Vikaspuri , New Delhi" + "  3PM - 6PM");
                                        mapping.put(R.id.rb31 , eventId.getText().toString() + "PVR Vikaspuri , New Delhi" + "  9PM - 12AM");


                                        mapping.put(R.id.rb12 , eventId.getText().toString() + "Cinepolis Janakpuri , New Delhi" + "  10 AM - 1 PM ");
                                        mapping.put(R.id.rb22 , eventId.getText().toString() + "Cinepolis Janakpuri , New Delhi" + "  3PM - 6PM");
                                        mapping.put(R.id.rb32 , eventId.getText().toString() + "Cinepolis Janakpuri , New Delhi" + "  9PM - 12AM");


                                        mapping.put(R.id.rb13 , eventId.getText().toString() + "PVR PunjabiBagh , New Delhi" + "  10 AM - 1 PM ");
                                        mapping.put(R.id.rb23 , eventId.getText().toString() + "PVR PunjabiBagh , New Delhi" + "  3PM - 6PM");
                                        mapping.put(R.id.rb33 , eventId.getText().toString() + "PVR PunjabiBagh , New Delhi" + "  9PM - 12AM");


                                        for (Map.Entry<Integer, String> entry : mapping.entrySet()) {
                                            Integer id = entry.getKey();
                                            String value = entry.getValue();

                                            EditText et = capacityView.findViewById(id);
                                            String capacityAssigned =  et.getText().toString();

                                            for(int no=1;no<=Integer.valueOf(capacityAssigned);no++)
                                            {
                                                String resultantkey = value + " - rb" + no;
                                                DependencyInjection.getEventHandler().addseatsinSeatAvailability(resultantkey);
                                            }

                                            DependencyInjection.getEventHandler().initialiseTotalSeats(value , capacityAssigned);
                                        }


                                        recreate();

                                    }
                                })
                                .create();
                        newAlertDialog.show();


                    }
                })
                .create();

        final View event_or_movie=lif.inflate(R.layout.event_or_movie,null);

        final AlertDialog myalertDialog=new AlertDialog.Builder(this)
                .setTitle("What do you want to add ??")
                .setView(event_or_movie)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        map.clear();
                        RadioGroup radioGroup = event_or_movie.findViewById(R.id.radiogroup);
                        int selectedId=-1;
                        selectedId = radioGroup.getCheckedRadioButtonId();

                        RadioButton selradioButton =  (RadioButton) radioGroup.findViewById(selectedId);

                        String text = (String) selradioButton.getText();
                        if(text.equals("  Add a Movie"))
                        {
                            map.put("typeOfEvent" , new AttributeValue("Movie"));
                        }
                        else
                        {
                            map.put("typeOfEvent" , new AttributeValue("Movie"));
                        }

                        detailsalertDialog.show();
                        poster.setTextIsSelectable(true);


                    }
                })
                .create();



        FloatingActionButton myfab = findViewById(R.id.fabcheck);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myalertDialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.managersignout, menu); return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent loginintent = AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build())
                        )
                        .build();

                startActivity(loginintent);
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("pm clear src.com.filmybonanza");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }

        return true;
    }

    public static ManagerAdapter getAdapter()
    {
        return ManagerActivity.adapter;
    }

}
