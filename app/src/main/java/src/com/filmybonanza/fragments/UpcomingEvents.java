package src.com.filmybonanza.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import src.com.filmybonanza.views.MovieAdapter;
import src.com.filmybonanza.R;
import src.com.filmybonanza.activities.ShowUserDetails;
import src.com.filmybonanza.model.Event;
import src.com.filmybonanza.singleton.DependencyInjection;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingEvents extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Event> arrayList=new ArrayList<>();
    FirebaseUser firebaseUser;
    MovieAdapter adapter;
    Intent loginintent;
    Context context;

    public UpcomingEvents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        recyclerView = v.findViewById(R.id.recycle);
        adapter=new MovieAdapter(arrayList, getActivity());;
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        EventHandler eventHandler=new EventHandler();
        ArrayList<Event>newarrayList= DependencyInjection.getEventHandler().getEvents("Event");

        arrayList.clear();
        arrayList.addAll(newarrayList);
        adapter.notifyDataSetChanged();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu) ;
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
                    runtime.exec("pm clear com.example.amazonmoviebooking");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.profile:
                Intent intent=new Intent(getActivity(), ShowUserDetails.class);
                getActivity().startActivity(intent);
                break;


            case R.id.Booking_History:


        }

        return true;
    }

}
