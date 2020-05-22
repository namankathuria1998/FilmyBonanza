package com.example.filmybonanza;

// This is a POJO class containing all the movie details
public class Movie {

    private String title , overview , poster , movieId;
    private boolean adult;

    public Movie(String title, String overview, String poster, String movieId , boolean adult) {
        this.title = title;
        this.overview = overview;
        this.poster = poster;
        this.adult = adult;
        this.movieId = movieId;
    }
}
