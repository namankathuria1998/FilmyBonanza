package src.com.filmybonanza;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.firebase.ui.auth.data.model.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;

import src.com.filmybonanza.activities.MainActivity;
import src.com.filmybonanza.dao.DynamodbImpl;
import src.com.filmybonanza.handler.EventHandler;
import src.com.filmybonanza.handler.UserHandler;
import src.com.filmybonanza.singleton.DependencyInjection;
import static org.assertj.core.api.Assertions.*;

public class DynamodbImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();




    @Test
    public void areticketsgettingDecreased()
    {

        EventHandler eventHandler= Mockito.mock(EventHandler.class);
        when(eventHandler.getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM"))
                .thenReturn("14").thenReturn("13");

        assertEquals("14",eventHandler.getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM"));
        verify(eventHandler, times(1)).getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM");

        doNothing().when(eventHandler).decreaseTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM",14,1);

        assertEquals("13",eventHandler.getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM"));
        verify(eventHandler, times(2)).getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM");


        verifyNoMoreInteractions(eventHandler);


//        String ticketsInitial = DependencyInjection.getEventHandler().getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM");
//
//        DependencyInjection.getEventHandler().decreaseTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM"
//                ,Integer.parseInt(ticketsInitial),1);
//
//        String ticketsNow = DependencyInjection.getEventHandler().getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM");
//
//        assertEquals(Integer.parseInt(ticketsInitial)-1 , Integer.parseInt(ticketsNow));

    }

    @Test
    public void isSeatMatrixChanging()
    {
        EventHandler eventHandler=Mockito.mock(EventHandler.class);
        when(eventHandler.getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM"))
                .thenReturn("AAAAAAAAAAAAAAA").thenReturn("AANAAAAAAAAAAAA");

        assertEquals("AAAAAAAAAAAAAAA",eventHandler.getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM"));
        verify(eventHandler, times(1)).getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM");

        doNothing().when(eventHandler).setSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM",2,'N');

        assertEquals("AANAAAAAAAAAAAA",eventHandler.getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM"));
        verify(eventHandler, times(2)).getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM");


        verifyNoMoreInteractions(eventHandler);

//        String initialMatrix = DependencyInjection.getEventHandler().getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM");
//        assertEquals('A',initialMatrix.charAt(13));
//        DependencyInjection.getEventHandler().setSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM",13,'N');
//        String finalmatrix = DependencyInjection.getEventHandler().getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM");
//        assertEquals('N',finalmatrix.charAt(13));
    }

    @Test
    public void isSeatGettingBooked()
    {
        EventHandler eventHandler=Mockito.mock(EventHandler.class);
        SeatAvailability seatAvailability=Mockito.mock(SeatAvailability.class);

        when(eventHandler.getSeatStatus("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1")).thenReturn(seatAvailability);

        when(eventHandler.getSeatStatus("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1").getIsBooked())
                .thenReturn("false").thenReturn("true");

        assertEquals("false",eventHandler.getSeatStatus("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1").getIsBooked());

        doNothing().when(eventHandler).setSeat("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1","true");

        assertEquals("true",eventHandler.getSeatStatus("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1").getIsBooked());


//        String initialStatus =  DependencyInjection.getEventHandler().getSeatStatus("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM - rb1").getIsBooked();
//        assertEquals("false",initialStatus);
//        DependencyInjection.getEventHandler().setSeat("37292979Cinepolis Janakpuri , New Delhi 9PM - 12AM - rb1","true");
//        String finalStatus = DependencyInjection.getEventHandler().getSeatStatus("37292979Cinepolis Janakpuri , New Delhi 9PM - 12AM - rb1").getIsBooked();
//        assertEquals("true",finalStatus);
    }




    @Test
    public void userHasSignedUp()
    {
        UserHandler userHandler=Mockito.mock(UserHandler.class);
        UserDetails userDetails=Mockito.mock(UserDetails.class);

        when(userHandler.getUserDetails("2D5rZI5lL5QnqeYEoTMwMcamzwk2")).thenReturn(userDetails);
        assertNotNull(userHandler.getUserDetails("2D5rZI5lL5QnqeYEoTMwMcamzwk2"));


//        UserDetails user = DependencyInjection.getUserHandler().getUserDetails("2D5rZI5lL5QnqeYEoTMwMcamzwk2");
//        assertNotNull(user);
    }



    @Test
    public void areNoOfSeatsAvailableCorrect()
    {
        EventHandler eventHandler=Mockito.mock(EventHandler.class);
        when(eventHandler.getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM")).thenReturn("14");
        assertEquals("14" , eventHandler.getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM"));

//        String tickets = DependencyInjection.getEventHandler().getNoOfAvailableTickets("909876Cinepolis Janakpuri , New Delhi  9PM - 12AM");
//        assertEquals("14" , tickets);
    }


    @Test
    public void isSeatMatrixCorrect()
    {
        EventHandler eventHandler=Mockito.mock(EventHandler.class);
        when(eventHandler.getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM")).thenReturn("AAAAAAAAAAAAAAA");
        assertEquals("AAAAAAAAAAAAAAA",eventHandler.getSeatMatrixInTickets("37292979Cinepolis Janakpuri , New Delhi  9PM - 12AM"));
//
//        String seatMatrix = DependencyInjection.getEventHandler().getSeatMatrixInTickets("37292979PVR Vikaspuri , New Delhi  3PM - 6PM");
//        String expected = "NANAAAAAAAAAAAA";
//
//        assertEquals(expected , seatMatrix);
    }




    @Test
    public void areUserDetailsUpdated()
    {


//        UserDetails Details = DependencyInjection.getUserHandler().getUserDetails("xTCkxvqPdtg3MJ7eaHNvKr6gvzM2");
//
//        Details.setUserName("Sunita Bhutani Kumar Kathuria");
//        Log.e("My name is", Details.getUserName());
//        DependencyInjection.getUserHandler().updateUserDetails("xTCkxvqPdtg3MJ7eaHNvKr6gvzM2",Details);
//        UserDetails newDetails = DependencyInjection.getUserHandler().getUserDetails("xTCkxvqPdtg3MJ7eaHNvKr6gvzM2");
//        assertThat(newDetails).isEqualToComparingFieldByField(Details);

    }


    @Test
    public void areUserDetailsCorrect()
    {


        UserDetails actual = DependencyInjection.getUserHandler().getUserDetails("8xkENKVyr3cxJMZacTWWI6vfnnr1");

        UserDetails expected = new UserDetails("naman kathuria","naman.kathuriait@gmail.com",
                null,"8xkENKVyr3cxJMZacTWWI6vfnnr1");

        assertThat(actual).isEqualToComparingFieldByField(expected);
    }



    @Test
    public void getTheUserBookings()
    {
        ArrayList<BookedEvent>actual = DependencyInjection.getUserHandler().getUserBookings("v0cErv47guYvNAM2Isy0gk2jeIc2");

        BookedEvent bookedEvent=new BookedEvent("1245",null,"2020-06-03","19:52 PM",
                "https://cdn.kalingatv.com/wp-content/uploads/2020/03/breaking-radhe-koimoi.jpg",
                "Radhe","v0cErv47guYvNAM2Isy0gk2jeIc2","25 June 2020","  9PM - 12AM","");

        assertThat(actual.get(0)).isEqualToComparingFieldByField(bookedEvent);
    }

    @Test
    public void canGetAllTheEvents()
    {
        ArrayList<Event>actual = DependencyInjection.getEventHandler().getEvents("Event");

        Event expected = new Event("Concert","Major COVID-19 virtual relief concert to feature Pearl Jam, Dave Matthews, Brandi Carlile and other Seattle stars"
        ,"https://blogmedia.evbstatic.com/wp-content/uploads/wpmulti/sites/8/2019/10/How-to-organise-music-concert.jpg"
        ,"5678","Event","5 June 2020");

        assertThat(actual.get(0)).isEqualToComparingFieldByField(expected);
    }

}
