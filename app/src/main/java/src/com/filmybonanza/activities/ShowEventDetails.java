package src.com.filmybonanza.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.services.dynamodbv2.AcquireLockOptions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClientOptions;
import com.amazonaws.services.dynamodbv2.CreateDynamoDBTableOptions;
import com.amazonaws.services.dynamodbv2.LockItem;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.model.BookedEvent;
import src.com.filmybonanza.model.Event;
import src.com.filmybonanza.views.NothingSelectedSpinnerAdapter;
import src.com.filmybonanza.R;
import src.com.filmybonanza.model.SeatAvailability;
import src.com.filmybonanza.client.DynamodbClient;
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
    boolean islockacquiredflag[]=new boolean[15];
    Map<Integer, Pair<String,Integer>>map=new HashMap<>();
    View seatmatrix;
    View newRadioButtonDialog;
    AlertDialog seatsmatrix;
    LockItem[]lock=new LockItem[15];
    public static View enterticketdetails;
    public static String seatsSelected;
    boolean [] isButtonAlradyChecked=new boolean[15];
    public static Timer timer;
    String temp;

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
        seatmatrix = lif.inflate(R.layout.seatmatrix, null);

        try {
            AmazonDynamoDBLockClient.createLockTableInDynamoDB(createDynamoDBTableOptions);
        }
        catch(Exception e)
        { }

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
        DependencyInjection.getEventHandler().displayEventDetails(event,this);

        final View mypayment = lif.inflate(R.layout.payment, null);

        FloatingActionButton fabsave=findViewById(R.id.fab);
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterticketdetails = lif.inflate(R.layout.enterticketdetails, null);
                newRadioButtonDialog=enterticketdetails;

                final AlertDialog alertDialog = new AlertDialog.Builder(ShowEventDetails.this)
                        .setTitle("Event Info ??")
                        .setView(enterticketdetails)
                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
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
                                text = spinner.getSelectedItem().toString();

                                RadioGroup radioGroup = enterticketdetails.findViewById(R.id.radioGroup);
                                int selectedId=-1;
                                selectedId = radioGroup.getCheckedRadioButtonId();

                                RadioButton selradioButton =  (RadioButton) radioGroup.findViewById(selectedId);
                                if(selradioButton.getText().equals("  10 AM - 1 PM ")) timings="  10 AM - 1 PM ";
                                else if(selradioButton.getText().equals("  3PM - 6PM")) timings="  3PM - 6PM";
                                else timings = "  9PM - 12AM";

                                temp = DependencyInjection.getEventHandler().getNoOfAvailableTickets(eventId+ text + timings);
                                noOfTickets = enterticketdetails.findViewById(R.id.nooftickets);
                                noTicketsavail = Integer.parseInt(DependencyInjection.getEventHandler().getNoOfAvailableTickets(
                                        eventId + text + timings));

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
                                    map.put(R.id.rb1,new Pair<>(" - rb1",0)); map.put(R.id.rb2,new Pair<>(" - rb2",1)); map.put(R.id.rb3,new Pair<>(" - rb3",2));
                                    map.put(R.id.rb4,new Pair<>(" - rb4",3)); map.put(R.id.rb5,new Pair<>(" - rb5",4)); map.put(R.id.rb6,new Pair<>(" - rb6",5));
                                    map.put(R.id.rb7,new Pair<>(" - rb7",6)); map.put(R.id.rb8,new Pair<>(" - rb8",7)); map.put(R.id.rb9,new Pair<>(" - rb9",8));
                                    map.put(R.id.rb10,new Pair<>(" - rb10",9)); map.put(R.id.rb11,new Pair<>(" - rb11",10)); map.put(R.id.rb12,new Pair<>(" - rb12",11));
                                    map.put(R.id.rb13,new Pair<>(" - rb13",12)); map.put(R.id.rb14,new Pair<>(" - rb14",13)); map.put(R.id.rb15,new Pair<>(" - rb15",14));

                                    rb1 = (RadioButton) seatmatrix.findViewById(R.id.rb1);
                                    rb2 = (RadioButton) seatmatrix.findViewById(R.id.rb2);

                                    if(seatmatrix.getParent()!=null)
                                        ((ViewGroup)seatmatrix.getParent()).removeView(seatmatrix);

                                    seatsmatrix = new AlertDialog.Builder(ShowEventDetails.this)
                                            .setTitle("Select Seats")
                                            .setView(seatmatrix)
                                            .setPositiveButton("Proceed to pay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    int count=0;
                                                    for(int no=0;no<15;no++)
                                                    {
                                                        if(no >= Integer.valueOf(temp)) continue;
                                                        if(islockacquiredflag[no]) count++;
                                                    }

                                                    Log.e("Total count is", String.valueOf(count));
                                                    if(count != Integer.parseInt(noOfTickets.getText().toString()))
                                                    {
                                                        for (Map.Entry<Integer, Pair<String,Integer>> entry : map.entrySet()) {
                                                            Integer key = entry.getKey();
                                                            Pair<String,Integer> value = entry.getValue();

                                                            if(value.second >= Integer.valueOf(temp)) continue;
                                                            if(islockacquiredflag[value.second]) {
                                                                try {
                                                                    client.releaseLock(lock[value.second]);
                                                                    islockacquiredflag[value.second] = false;
                                                                    DependencyInjection.getEventHandler().setSeat(eventId + text + timings + value.first, "false");
                                                                    DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId + text + timings, value.second, 'A');
                                                                    ((RadioButton)seatmatrix.findViewById(key)).setChecked(false);
                                                                    isButtonAlradyChecked[value.second]=false;

                                                                }
                                                                catch (Exception e)
                                                                {

                                                                }
                                                            }
                                                        }
                                                       AlertDialog sametickets = new AlertDialog.Builder(ShowEventDetails.this)
                                                                .setTitle("Please selct same no of tickets")
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                            recreate();
                                                                    }
                                                                })
                                                                .create();
                                                        sametickets.show();
                                                    }
                                                    else startPayment();
                                                }
                                            })
                                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    for (Map.Entry<Integer, Pair<String,Integer>> entry : map.entrySet()) {
                                                        Integer key = entry.getKey();
                                                        Pair<String,Integer> value = entry.getValue();
                                                        if(value.second >= Integer.valueOf(temp)) continue;

                                                        if(((RadioButton)seatmatrix.findViewById(key)).isChecked()) {
                                                           try {
                                                               client.releaseLock(lock[value.second]);
                                                               islockacquiredflag[value.second] = false;
                                                               DependencyInjection.getEventHandler().setSeat(eventId + text + timings + value.first, "false");
                                                               DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId + text + timings, value.second, 'A');
                                                               ((RadioButton)seatmatrix.findViewById(key)).setChecked(false);
                                                               isButtonAlradyChecked[value.second]=false;
                                                           }
                                                           catch (Exception e)
                                                           {

                                                           }
                                                        }
                                                    }
                                                    recreate();
                                                }
                                            })
                                            .create();

                                    String matrix = DependencyInjection.getEventHandler().getSeatMatrixInTickets(eventId+ text + timings);

                                    for (Map.Entry<Integer, Pair<String,Integer>> entry : map.entrySet()) {
                                        Integer key = entry.getKey();
                                        Pair<String,Integer> value = entry.getValue();

                                        if(value.second >= Integer.valueOf(temp)) continue;
                                            if (matrix.charAt(value.second) == 'N') {
                                                        ((RadioButton)seatmatrix.findViewById(key)).setChecked(true);
                                                         isButtonAlradyChecked[value.second]=true;
                                                        ((RadioButton)seatmatrix.findViewById(key)).setText("Already Booked");
                                                        Log.e("char at ", "YES");
                                            }
                                        }
                                    int notAvail=0;
                                    String checkhowmanybooked = DependencyInjection.getEventHandler().getSeatMatrixInTickets(eventId+ text + timings);

                                    for(int tindex=0;tindex<checkhowmanybooked.length();tindex++)
                                    {
                                        if(checkhowmanybooked.charAt(tindex) == 'N')
                                            notAvail++;
                                    }

                                    Log.e("there avaialable",  temp);
                                    Log.e("Not there avaialable", String.valueOf(notAvail) );
                                    for (Map.Entry<Integer, Pair<String,Integer>> entry : map.entrySet()) {
                                        Integer key = entry.getKey();
                                        Pair<String,Integer> value = entry.getValue();

                                        if(value.second >= (Integer.valueOf(temp) +notAvail))
                                        {
                                            ((RadioButton)seatmatrix.findViewById(key)).setVisibility(View.GONE);
                                        }
                                    }
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

                ArrayAdapter<String>adapter = new ArrayAdapter<String>(ShowEventDetails.this,
                        android.R.layout.simple_spinner_item,items);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setPrompt("Select the Location!");

                spinner.setAdapter(
                        new NothingSelectedSpinnerAdapter(
                                adapter,
                                R.layout.contact_spinner_row_nothing_selected,
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

        seatsSelected="";
        for (Map.Entry<Integer, Pair<String,Integer>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Pair<String,Integer> value = entry.getValue();
            if(value.second >= Integer.valueOf(temp)) continue;
            if(((RadioButton)seatmatrix.findViewById(key)).isChecked()) {

               try {
                   client.releaseLock(lock[value.second]);
                   islockacquiredflag[value.second] = false;
                   seatsSelected += (value.first );
               }
               catch (Exception e)
               {

               }
                ((RadioButton)seatmatrix.findViewById(key)).setChecked(false);
                isButtonAlradyChecked[value.second]=false;
            }
        }

        final String newSeatsSelected = seatsSelected;
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
                intent1.putExtra("SeatsSelected",newSeatsSelected);
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

        String whichbutton = map.get(v.getId()).first;
       SeatAvailability obj = DependencyInjection.getEventHandler().getSeatStatus(eventId+text+timings+whichbutton) ;

        if(islockacquiredflag[map.get(v.getId()).second])
        {
            RadioButton rbt = ((RadioButton)seatmatrix.findViewById(v.getId()));
            rbt.setChecked(false);
            client.releaseLock(lock[map.get(v.getId()).second]);
            islockacquiredflag[map.get(v.getId()).second] = false;
            DependencyInjection.getEventHandler().setSeat(eventId + text + timings + map.get(v.getId()).first, "false");
            DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId + text + timings, map.get(v.getId()).second, 'A');
            isButtonAlradyChecked[map.get(v.getId()).second] = false;
        }

       else if(obj.getIsBooked().equals("true"))
       {
           final AlertDialog already = new AlertDialog.Builder(ShowEventDetails.this)
                   .setTitle("This seat has already been booked")
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   })
                   .create();
           already.show();
       }
       else {
           long curtime = System.currentTimeMillis()/1000L + 20;
           Map<String,AttributeValue>additionamap=new HashMap<>();
            additionamap.put("time",new AttributeValue().withN(String.valueOf(curtime)));
           AcquireLockOptions lockOptions = AcquireLockOptions.builder(eventId + text + timings + whichbutton)
                   .withShouldSkipBlockingWait(true)
                   .withAdditionalAttributes(additionamap)
                   .build();

           try {
               client = new AmazonDynamoDBLockClient(
                       AmazonDynamoDBLockClientOptions.builder(DynamodbClient.getClient(), "LockTable")
                               .withTimeUnit(TimeUnit.MILLISECONDS)
                               .withLeaseDuration(10L)
                               .build());

               LockItem lockItem = client.acquireLock(lockOptions);
               lock[map.get(v.getId()).second] = lockItem;
               DependencyInjection.getEventHandler().setSeat(eventId + text + timings + whichbutton, "true");
               DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId + text + timings, map.get(v.getId()).second, 'N');
               islockacquiredflag[map.get(v.getId()).second] = true;
               isButtonAlradyChecked[map.get(v.getId()).second]=true;

//               Timer timer = new Timer();
//
//               timer.schedule(new TimerTask() {
//                   @Override
//                   public void run() {
//
//                       ShowEventDetails.this.runOnUiThread(new Runnable() {
//                           @Override
//                           public void run() {
//                               try {
//                                   Pair<String, Integer> value = map.get(v.getId());
//                                   islockacquiredflag[value.second] = false;
//                                   DependencyInjection.getEventHandler().setSeat(eventId + text + timings + value.first, "false");
//                                   DependencyInjection.getEventHandler().setSeatMatrixInTickets(eventId + text + timings, value.second, 'A');
//                                   ((RadioButton) seatmatrix.findViewById(v.getId())).setChecked(false);
//                                   isButtonAlradyChecked[value.second] = false;
//                               }
//                               catch (Exception e)
//                               {
//                                   Log.e("TTL my Error", e.toString() );
//                               }
//                           }
//                       });
//
//                   }
//               }, 10*60*1000);

           }
           catch (Exception e) {
               Log.e("Lock not given1", e.toString());
               seatalreadylocked.show();
               ((RadioButton) seatmatrix.findViewById(v.getId())).setChecked(false);
               isButtonAlradyChecked[map.get(v.getId()).second]=false;
           }
       }
    }

    public void onRadioButtonClicked(View v)
    {
        RadioGroup radioGroup = enterticketdetails.findViewById(R.id.radioGroup);
        int selectedId=-1;
        selectedId = radioGroup.getCheckedRadioButtonId();

        RadioButton selradioButton =  (RadioButton) radioGroup.findViewById(selectedId);

        String mytimings;
        if(selradioButton.getText().equals("  10 AM - 1 PM ")) mytimings="  10 AM - 1 PM ";
        else if(selradioButton.getText().equals("  3PM - 6PM")) mytimings="  3PM - 6PM";
        else mytimings = "  9PM - 12AM";

        Spinner myspinner = (Spinner) enterticketdetails.findViewById(R.id.myspinner);
        String mytext = spinner.getSelectedItem().toString();

        Log.e("checker", eventId+mytext+mytimings );
        String remtickets = DependencyInjection.getEventHandler().getNoOfAvailableTickets(eventId+mytext+mytimings) ;
        TextView tv=enterticketdetails.findViewById(R.id.select);
        tv.setText("Seats available :" + remtickets);
    }
}
