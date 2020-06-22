package src.com.filmybonanza.dao;

import android.content.Context;

import java.util.ArrayList;

import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.SeatAvailability;
import src.com.filmybonanza.UserDetails;

public interface DynamodbDao {

    UserDetails getUserDetails(String userId,Context context);
    boolean updateUserDetails(String userId , UserDetails newUserDetails);
    void addEvent(Event event);
    void bookMovie(BookedEvent bookedEvent);
    void displayEventDetails(Event event, Context context);
    ArrayList<Event> getEvents(String eventtype);
    ArrayList<BookedEvent> getUserBookings(String userId);
    String getNoOfAvailableTickets(String key);
    void decreaseTickets(String key,int ticketsavail,int ticketsToBeBooked);
    SeatAvailability getSeatStatus(String key);
    void setSeat(String key,String whatToBeSet);
    String getSeatMatrixInTickets(String locatioon_timings);
    void setSeatMatrixInTickets(String locatioon_timings, int i,char ch);
    void addTimeDuration(String key,Long timeDuration);
    void initialiseTotalSeats(String key,String noOfSeats);
    void addseatsinSeatAvailability(String key);

}
