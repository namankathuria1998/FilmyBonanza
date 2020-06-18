package src.com.filmybonanza.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClientOptions;
import com.amazonaws.services.dynamodbv2.AcquireLockOptions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClientOptions;
import com.amazonaws.services.dynamodbv2.CreateDynamoDBTableOptions;
import com.amazonaws.services.dynamodbv2.LockItem;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.LockNotGrantedException;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.NothingSelectedSpinnerAdapter;
import src.com.filmybonanza.R;
import src.com.filmybonanza.SeatAvailability;
import src.com.filmybonanza.dynamodbClient.DynamodbClient;
import src.com.filmybonanza.singleton.DependencyInjection;


// this activity / screen will display the details of a particular movie
public class ShowEventDetails extends AppCompatActivity implements PaymentResultListener {

    AlertDialog codealert;
    int noTicketsavail;
    Spinner spinner;
    TextView title,overview,date;
    ImageView iv;
    BookedEvent bookedEvent;
    EditText noOfTickets;
    String phoneVerificationId;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mAuth;
    LayoutInflater lif;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    AuthCredential credential;
    String text;
    String timings;
    AlertDialog seatalreadylocked;
    AmazonDynamoDBLockClient client;
    RadioButton rb1,rb2;
    String eventId;
    LockItem lock1,lock2,lock3,lock4,lock5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        lif = LayoutInflater.from(ShowEventDetails.this);

        CreateDynamoDBTableOptions createDynamoDBTableOptions=CreateDynamoDBTableOptions.builder(DynamodbClient.getClient(),
                new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L) ,"LockTable").build();
//
        try {
            AmazonDynamoDBLockClient.createLockTableInDynamoDB(createDynamoDBTableOptions);
        }
        catch(Exception e)
        { }

        client = new AmazonDynamoDBLockClient(
                AmazonDynamoDBLockClientOptions.builder(DynamodbClient.getClient(), "LockTable")
                        .withTimeUnit(TimeUnit.SECONDS)
                        .withLeaseDuration(60L)
                        .build());


        seatalreadylocked = new AlertDialog.Builder(ShowEventDetails.this)
                .setTitle("Sorry , This seat is being booked by someone else!!!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .create();

        Checkout.preload(getApplicationContext());

        final Intent intent=getIntent();
        String json = intent.getStringExtra("key");
        Gson gson=new Gson();
        final Event event = gson.fromJson(json,Event.class);
        eventId = event.getEventId();
//        final EventActivity eventActivity=new EventActivity();
        DependencyInjection.getEventHandler().displayEventDetails(event,this);

        final View mypayment = lif.inflate(R.layout.payment, null);

        FloatingActionButton fabsave=findViewById(R.id.fab);
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final View enterticketdetails = lif.inflate(R.layout.enterticketdetails, null);

                final AlertDialog alertDialog = new AlertDialog.Builder(ShowEventDetails.this)
                        .setTitle("Event Info ??")
                        .setView(enterticketdetails)
                        .setPositiveButton("Confirm Ticket", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Date today = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String dateToStr = format.format(today);

                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                                Date currentLocalTime = cal.getTime();
                                DateFormat date = new SimpleDateFormat("HH:mm a");

                                date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                                String localTime = date.format(currentLocalTime);

                                RadioGroup radioGroup = enterticketdetails.findViewById(R.id.radioGroup);
                                int selectedId=-1;
                                selectedId = radioGroup.getCheckedRadioButtonId();

                                RadioButton selradioButton =  (RadioButton) radioGroup.findViewById(selectedId);


                                if(selradioButton.getText().equals("  10 AM - 1 PM ")) timings="  10 AM - 1 PM ";
                                else if(selradioButton.getText().equals("  3PM - 6PM")) timings="  3PM - 6PM";
                                else timings = "  9PM - 12AM";

//                                EditText location=enterticketdetails.findViewById(R.id.location);
                                text = spinner.getSelectedItem().toString();
                                noOfTickets = enterticketdetails.findViewById(R.id.nooftickets);

                                noTicketsavail = Integer.parseInt(DependencyInjection.getEventHandler().getNoOfAvailableTickets(text + timings));




                                bookedEvent = new BookedEvent(event.getEventId(),
                                        event.getTypeOfEvent(),dateToStr,localTime,
                                        event.getPoster(),event.getTitle(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        event.getDate(),timings,text);

                                TextView fee=mypayment.findViewById(R.id.fee);
                                fee.setText("Ticket Fee :- Rs "+(200*Integer.valueOf(noOfTickets.getText().toString())));


                                TextView totalfee=mypayment.findViewById(R.id.totalfee);
                                totalfee.setText("Total Fee :- Rs "+(200*Integer.valueOf(noOfTickets.getText().toString())));

                                if(noTicketsavail >= Integer.parseInt(noOfTickets.getText().toString()))
                                {

                                    final View seatmatrix = lif.inflate(R.layout.seatmatrix, null);
                                    rb1 = (RadioButton) seatmatrix.findViewById(R.id.rb1);
                                    rb2 = (RadioButton) seatmatrix.findViewById(R.id.rb2);
                                    AlertDialog seatsmatrix = new AlertDialog.Builder(ShowEventDetails.this)
                                            .setTitle("Select Seats")
                                            .setView(seatmatrix)
                                            .setPositiveButton("Proceed to pay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startPayment();
                                                }
                                            })
                                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    if(rb1.isChecked()){
                                                        client.releaseLock(lock1);
                                                        DependencyInjection.getEventHandler().setSeat(eventId+text+timings+" - rb1","false");
                                                        DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId+text+timings,0,'A');
                                                    }

                                                    if(rb2.isChecked()){
                                                        client.releaseLock(lock2);
                                                        DependencyInjection.getEventHandler().setSeat(eventId+text+timings+" - rb2","false");
                                                        DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId+text+timings,1,'A');
                                                    }

                                                }
                                            })
                                            .create();

                                    String matrix = DependencyInjection.getEventHandler().getSeatMatrixInTickets(eventId+ text + timings);

//                                        for (int ind = 0; ind < matrix.length(); ind++) {
                                    if (matrix.charAt(0) == 'N') {
                                        rb1.setChecked(true);
                                        rb1.setText("Already Booked");
//                                                rb1.setHighlightColor(Color.parseColor("#FF0000"));
                                        Log.e("char at ", "YES");
                                    }
                                    if (matrix.charAt(1) == 'N') {
                                        rb2.setChecked(true);
                                        rb2.setText("Already Booked");
                                        //                                                rb1.setHighlightColor(Color.parseColor("#FF0000"));
                                        Log.e("char at ", "YES");
                                    }
//                                        }



                                    seatsmatrix.show();

                                }
                                else
                                {
                                    View sorryview = lif.inflate(R.layout.ticketnotavailable, null);
                                    final AlertDialog sorry = new AlertDialog.Builder(ShowEventDetails.this)
                                            .setView(sorryview)
                                            .setTitle("Sorry !!!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                            .create();
                                    sorry.show();

                                }

                            }
                        })
                        .create();


                String[] items = new String[]{"PVR Vikaspuri , New Delhi", "Cinepolis Janakpuri , New Delhi",
                        "PVR PunjabiBagh , New Delhi"};
                spinner = (Spinner) enterticketdetails.findViewById(R.id.myspinner);
//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);

                ArrayAdapter<String>adapter = new ArrayAdapter<String>(ShowEventDetails.this,
                        android.R.layout.simple_spinner_item,items);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setPrompt("Select the Location!");

                spinner.setAdapter(
                        new NothingSelectedSpinnerAdapter(
                                adapter,
                                R.layout.contact_spinner_row_nothing_selected,
                                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                ShowEventDetails.this));


                alertDialog.show();
            }


        });
    }

    public void startPayment() {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_JXGPHkpGvkEO76");

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Merchant Name");

            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_id");
            options.put("currency", "INR");

            options.put("amount", "1000");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"payment Successful",Toast.LENGTH_SHORT);
        DependencyInjection.getUserHandler().bookMovie(bookedEvent);
        DependencyInjection.getEventHandler().decreaseTickets(eventId+text+timings,noTicketsavail
                ,Integer.parseInt(noOfTickets.getText().toString()));
        client.releaseLock(lock1);

        final ProgressDialog dialog = new ProgressDialog(ShowEventDetails.this);
        dialog.setTitle("Loading the Ticket");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 5000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                String json = DependencyInjection.getGson().toJson(bookedEvent);
                Intent intent1 = new Intent(ShowEventDetails.this, Ticket.class);
                intent1.putExtra("key" , json);
                intent1.putExtra("fee" , String.valueOf(200*Integer.valueOf(noOfTickets.getText().toString())));
                ShowEventDetails.this.startActivity(intent1);
            }
        }, delayInMillis);

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this,"payment failed",Toast.LENGTH_SHORT);
        DependencyInjection.getEventHandler().setSeat(eventId+text+timings+" - rb1","false");
    }


    public void alreadyInProgress(View v) throws InterruptedException, IOException {

        switch(v.getId())
        {
            case R.id.rb1:

                AcquireLockOptions lockOptions1 = AcquireLockOptions.builder(eventId+text+timings+" - rb1")
                        .withShouldSkipBlockingWait(true).build();

                try {
                    lock1 = client.acquireLock(lockOptions1);
                    DependencyInjection.getEventHandler().setSeat(eventId+text+timings+" - rb1","true");
                    DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId+text+timings,0,'N');
                }
                catch (Exception e)
                {
                    Log.e("Lock not given1", e.toString() );
                    seatalreadylocked.show();
                    rb1.setChecked(false);
                }

                break;

            case R.id.rb2:

                AcquireLockOptions lockOptions2 = AcquireLockOptions.builder(eventId+text+timings+" - rb2")
                        .withShouldSkipBlockingWait(true).build();

                try {
                    lock2 = client.acquireLock(lockOptions2);
                    DependencyInjection.getEventHandler().setSeat(eventId+text+timings+" - rb2","true");
                    DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId+text+timings,1,'N');
                }
                catch (Exception e)
                {
                    Log.e("Lock not given2", e.toString() );
                    seatalreadylocked.show();
                    rb2.setChecked(false);
                }
                break;
            default:break;
        }
    }

}
