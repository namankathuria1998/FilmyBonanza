package com.example.filmybonanza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MovieBookings extends AppCompatActivity {

    Context context;
    public MovieBookings(Context context) {
        this.context = context;
    }

    int totalBookedMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);
    }


    public Movie[] getUserBookings(String userId)
    {
        // this method will accept the user details and then
        // return a list of all the movies which have been
        // previously booked by that user

        return  new Movie[totalBookedMovies];
    }

    public void displayUserBookings(String userId)
    {
        // this method will take in a userId and
        // then display the movies previously booked by the user on this screen /

        Toast.makeText(context,"This will display user bookings",Toast.LENGTH_LONG).show();
    }


    public void addMovie(Movie movie)
    {
        // this method takes in a movie object
        // and adds it to the Movie database
    }

    public void bookMovie(String movieId)
    {
        // this method will take in a movieId.
        // it will parse the booking details and the user will be able to
        // book the ticket for the movie having this movieId

        // this method will also genearate the ticket for the booked movie
    }


}
