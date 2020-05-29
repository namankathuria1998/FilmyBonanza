package src.com.filmybonanza.handler;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.dao.DynamodbImpl;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.R;

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


}
