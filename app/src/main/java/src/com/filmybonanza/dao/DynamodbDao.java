package src.com.filmybonanza.dao;

import android.content.Context;

import java.util.ArrayList;

import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.UserDetails;

public interface DynamodbDao {

    UserDetails getUserDetails(String userId, Context context);
    void updateUserDetails(String userId , UserDetails newUserDetails);
    void addEvent(Event event);
    void bookMovie(BookedEvent bookedEvent);
    void displayEventDetails(Event event, Context context);
    ArrayList<Event> getEvents(String eventtype);
    ArrayList<BookedEvent> getUserBookings(String userId);

}