package com.example.filmybonanza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// this activity / screen will display the details of a particular movie
public class ShowMovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
    }


    public void displayMovieDetails(Movie movie)
    {
        // This method will accept a movie object and
        // then display the details of this Movie on the screen
    }

}
