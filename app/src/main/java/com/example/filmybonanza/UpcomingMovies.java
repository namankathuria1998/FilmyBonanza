package com.example.filmybonanza;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.glue.model.CreateTableRequest;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingMovies extends Fragment {


//    RecyclerView recyclerView;
//    MovieAdapter adapter;
//    ArrayList<UserDetails> arrayList=new ArrayList<>();
//    FirebaseUser firebaseUser;
    Intent loginintent;
    Context context;
;
    public UpcomingMovies(Context context) {
        this.context = context;
    }

    public UpcomingMovies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_movies, container, false);
    }


    public void displayMovies()
    {
        // this method will display the list of upcoming movies and events
        // on this screen/activity


    }
}
