package src.com.filmybonanza.handler;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.model.SeatAvailability;
import src.com.filmybonanza.dao.DynamodbImpl;
import src.com.filmybonanza.model.Event;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

public class EventHandler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_handler);
    }

    public void addEvent(Event event) {
        // this method takes in a movie object
        // and adds it to the Movie database

        DynamodbImpl.dynamodbDao.addEvent(event);
    }


    public void displayEventDetails(Event event, Context context)
    {
        // This method will accept a movie object and
        // then display the details of this Movie on the screen

        DynamodbImpl.dynamodbDao.displayEventDetails(event,context);
    }

    public ArrayList<Event> getEvents(String eventtype)
    {
        // this method will return the entire list of upcoming movies and events

        ArrayList<Event>events = DynamodbImpl.dynamodbDao.getEvents(eventtype);
        return events;
    }

    public String getNoOfAvailableTickets(String key)
    {
        return DynamodbImpl.dynamodbDao.getNoOfAvailableTickets(key);
    }

    public void decreaseTickets(String key,int ticketsavail,int ticketsToBeBooked)
    {
        DynamodbImpl.dynamodbDao.decreaseTickets(key,ticketsavail,ticketsToBeBooked);
    }

    public SeatAvailability getSeatStatus(String key)
    {
        return DynamodbImpl.dynamodbDao.getSeatStatus(key);
    }
    public void setSeat(String key,String whatToBeSet)
    {
        DynamodbImpl.dynamodbDao.setSeat(key,whatToBeSet);
    }

    public String getSeatMatrixInTickets(String locatioon_timings)
    {
        return DynamodbImpl.dynamodbDao.getSeatMatrixInTickets(locatioon_timings);
    }
    public void setSeatMatrixInTickets(String locatioon_timings,int i,char ch) {

        DynamodbImpl.dynamodbDao.setSeatMatrixInTickets(locatioon_timings,i,ch);
    }

    public void addTimeDuration(String key,Long timeDuration)
    {
        DependencyInjection.getEventHandler().addTimeDuration(key,timeDuration);
    }

    public void initialiseTotalSeats(String key, String noOfSeats)
    {
        DynamodbImpl.dynamodbDao.initialiseTotalSeats(key,noOfSeats);
    }

    public void addseatsinSeatAvailability(String key)
    {
        DynamodbImpl.dynamodbDao.addseatsinSeatAvailability(key);
    }

    public void deleteUpcomingMovies(String key)
    {
        DynamodbImpl.dynamodbDao.deleteUpcomingMovies(key);
    }

    public void deleteMovieFromBookinghistory(String key)
    {
        DynamodbImpl.dynamodbDao.deleteMovieFromBookinghistory(key);
    }

    public void addNewUpcomingMovie(String eventId,String date,String title,String poster,String summaryedit,String typeOfEvent)
    {
        DynamodbImpl.dynamodbDao.addNewUpcomingMovie( eventId, date, title, poster, summaryedit,typeOfEvent);
    }
}
