package src.com.filmybonanza.dao;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import src.com.filmybonanza.AvailableTickets;
import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.R;
import src.com.filmybonanza.SeatAvailability;
import src.com.filmybonanza.singleton.DependencyInjection;
import src.com.filmybonanza.dynamodbClient.DynamodbClient;
import src.com.filmybonanza.UserDetails;

public class DynamodbImpl implements DynamodbDao {

    public static DynamodbDao dynamodbDao=new DynamodbImpl();

    @Override
    public UserDetails getUserDetails(String userId, Context context) {

        GetItemRequest getItemRequest=new GetItemRequest();
        getItemRequest.setTableName("UsersDetails");
        Map<String, AttributeValue> map=new HashMap<String,AttributeValue>();
        map.put("uid",new AttributeValue(userId));
        getItemRequest.setKey(map);

        GetItemResult getItemResult = DynamodbClient.getClient().getItem(getItemRequest);
        if(getItemResult == null) return null;

        Map<String,AttributeValue>item = getItemResult.getItem();
        if(item==null) return null;

        return DependencyInjection.getUtilClass().singleResponseTransformer(item,UserDetails.class);
    }


    @Override
    public void updateUserDetails(String userId, UserDetails newUserDetails) {

        UpdateItemRequest updateItemRequest=new UpdateItemRequest();
        updateItemRequest.setTableName("UsersDetails");
        Map<String,AttributeValue>map=new HashMap<>();
        map.put("uid",new AttributeValue(userId));
        updateItemRequest.setKey(map);

        Map<String,String>names=new HashMap<>();
        names.put("#name","userName");
        names.put("#email","userEmail");
        names.put("#phoneNo","UserPhoneNo");
        updateItemRequest.setExpressionAttributeNames(names);

        Map<String, AttributeValue>values=new HashMap<>();
        values.put(":val1",new AttributeValue(newUserDetails.getUserName()));
        values.put(":val2",new AttributeValue(newUserDetails.getUserEmail()));
        values.put(":val3",new AttributeValue(newUserDetails.getUserPhoneNo()));
        updateItemRequest.setExpressionAttributeValues(values);

        updateItemRequest.setUpdateExpression("set #email = :val2 ,#name = :val1 , #phoneNo = :val3");

        try {
            DynamodbClient.getClient().updateItem(updateItemRequest);
        }
        catch (Exception e)
        {
            Log.e("No change", "hmmm" );

            Log.e("catch", e.toString() );
        }
    }


    @Override
    public void addEvent(Event event) {

        PutItemRequest putItemRequest=new PutItemRequest();

        Map<String, AttributeValue> map = new HashMap<String,AttributeValue>();
        map.put("eventId",new AttributeValue(event.getEventId()));
        map.put("title",new AttributeValue(event.getTitle()));
        map.put("summary",new AttributeValue(event.getSummary()));
        map.put("type",new AttributeValue(event.getTypeOfEvent()));
        map.put("date" , new AttributeValue(event.getDate()));

        putItemRequest.setItem(map);
        putItemRequest.setTableName("Events");

        DynamodbClient.getClient().putItem(putItemRequest);
    }


    @Override
    public void bookMovie(BookedEvent bookedEvent) {

        PutItemRequest putItemRequest=new PutItemRequest();
        putItemRequest.setTableName("UsersBookings");

        Map<String, AttributeValue>map=new HashMap<>();
        map.put("eventId" , new AttributeValue(bookedEvent.getEventId()));
        map.put("uid" , new AttributeValue(bookedEvent.getUid()));
        map.put("dateOfbooking" , new AttributeValue(bookedEvent.getDateOfbooking()));
        map.put("timeOfBooking" , new AttributeValue(bookedEvent.getTimeOfBooking()));
        map.put("poster" , new AttributeValue(bookedEvent.getPoster()));
        map.put("title" , new AttributeValue(bookedEvent.getTitle()));
        map.put("date",new AttributeValue(bookedEvent.getDate()));
        map.put("timings",new AttributeValue(bookedEvent.getTimings()));
        map.put("location",new AttributeValue(bookedEvent.getLocation()));
        putItemRequest.setItem(map);

        try {
            DynamodbClient.getClient().putItem(putItemRequest);
        }
        catch (Exception e)
        {
            Log.e("book moviw", e.toString() );
        }
    }

    @Override
    public void displayEventDetails(Event event, Context context) {

        TextView title = (TextView) ((Activity)context).findViewById(R.id.tit);
        TextView overview = (TextView) ((Activity)context).findViewById(R.id.overview);
        TextView date = (TextView) ((Activity)context).findViewById(R.id.myrelease);
        ImageView iv= (ImageView) ((Activity)context).findViewById(R.id.img);

        Picasso.get().load(event.getPoster()).into(iv);
        overview.setText("Summary :- "+event.getSummary());
        date.setText("Date :-  " + event.getDate());
        title.setText("Title :-  " + event.getTitle());
    }

    @Override
    public ArrayList<Event> getEvents(String eventtype) {

        QueryRequest queryRequest=new QueryRequest();
        queryRequest.setIndexName("typeOfEvent-index");
        queryRequest.setTableName("Events");

        Map<String,AttributeValue>map=new HashMap<String,AttributeValue>();
        Map<String,String>mapname=new HashMap<String,String>();
        mapname.put("#valtype","typeOfEvent");
        queryRequest.setExpressionAttributeNames(mapname);

        if(eventtype.equals("Movie")) {
            map.put(":val1",new AttributeValue("Movie"));
            queryRequest.setKeyConditionExpression("#valtype = :val1");
        }
        else {
            map.put(":val2",new AttributeValue("Event"));
            queryRequest.setKeyConditionExpression("#valtype = :val2");
        }

        queryRequest.setExpressionAttributeValues(map);
        QueryResult queryResult=queryResult = DynamodbClient.getClient().query(queryRequest);

        ArrayList<Map<String,AttributeValue>> response= (ArrayList<Map<String, AttributeValue>>) queryResult.getItems();
        return DependencyInjection.getUtilClass().multipleResponseTransformer(response,Event.class);

    }

    @Override
    public ArrayList<BookedEvent> getUserBookings(String userId) {

        QueryRequest queryRequest=new QueryRequest();
        queryRequest.setTableName("UsersBookings");
        queryRequest.setIndexName("UsersBookings-index");

        Map<String,AttributeValue>values=new HashMap<>();
        values.put(":userid",new AttributeValue(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        queryRequest.setExpressionAttributeValues(values);

        Map<String,String>names=new HashMap<>();
        names.put("#id","uid");
        queryRequest.setExpressionAttributeNames(names);

        queryRequest.setKeyConditionExpression("#id = :userid");
        QueryResult queryResult = DynamodbClient.getClient().query(queryRequest);

        if(queryResult == null) return null;
        ArrayList<Map<String,AttributeValue>>response = (ArrayList<Map<String, AttributeValue>>) queryResult.getItems();
        if(response==null) return null;
        return DependencyInjection.getUtilClass().multipleResponseTransformer(response , BookedEvent.class);

    }


    @Override
    public String getNoOfAvailableTickets(String key) {
        GetItemRequest getItemRequest=new GetItemRequest();
        getItemRequest.setTableName("Tickets");
        Map<String,AttributeValue>map=new HashMap<>();
        map.put("location_timings",new AttributeValue(key));
        getItemRequest.setKey(map);

        GetItemResult getItemResult = DynamodbClient.getClient().getItem(getItemRequest);
        Map<String,AttributeValue>response = getItemResult.getItem();
        AvailableTickets obj = DependencyInjection.getUtilClass().singleResponseTransformer(response , AvailableTickets.class);
        return obj.getAvailTickets();
    }

    @Override
    public void decreaseTickets(String key,int ticketsavail,int ticketsToBeBooked) {
        UpdateItemRequest updateItemRequest=new UpdateItemRequest();
        updateItemRequest.setTableName("Tickets");

        Map<String,AttributeValue>map=new HashMap<>();
        map.put("location_timings",new AttributeValue(key));
        updateItemRequest.setKey(map);

        Map<String,String>names=new HashMap<>();
        names.put("#avail","availTickets");
        updateItemRequest.setExpressionAttributeNames(names);

        Map<String,AttributeValue>values=new HashMap<>();
        values.put(":val1",new AttributeValue(String.valueOf(ticketsavail-ticketsToBeBooked)));
        updateItemRequest.setExpressionAttributeValues(values);

        updateItemRequest.setUpdateExpression("set #avail = :val1");
        DynamodbClient.getClient().updateItem(updateItemRequest);
    }


    public SeatAvailability getSeatStatus(String key)
    {
        GetItemRequest getItemRequest=new GetItemRequest();
        getItemRequest.setTableName("SeatAvailability");
        Map<String, AttributeValue> map=new HashMap<>();
        map.put("location_timings_seat",new AttributeValue(key));
        getItemRequest.setKey(map);

        GetItemResult getItemResult = DynamodbClient.getClient().getItem(getItemRequest);
        Map<String,AttributeValue>response = getItemResult.getItem();
        SeatAvailability obj = DependencyInjection.getUtilClass().singleResponseTransformer(response,SeatAvailability.class);
        return obj;
    }

    @Override
    public void setSeat(String key,String whatToBeSet) {
        UpdateItemRequest updateItemRequest=new UpdateItemRequest();
        updateItemRequest.setTableName("SeatAvailability");

        Map<String,String>names = new HashMap<>();
        names.put("#name","isBooked");
        updateItemRequest.setExpressionAttributeNames(names);

        Map<String,AttributeValue>values=new HashMap<>();
        values.put(":val",new AttributeValue(whatToBeSet));
        updateItemRequest.setExpressionAttributeValues(values);

        Map<String,AttributeValue>keymap=new HashMap<>();
        keymap.put("location_timings_seat",new AttributeValue(key));
        updateItemRequest.setKey(keymap);

        updateItemRequest.setUpdateExpression("set #name = :val");
        DynamodbClient.getClient().updateItem(updateItemRequest);
    }


    @Override
    public String getSeatMatrixInTickets(String locatioon_timings) {

        GetItemRequest getItemRequest=new GetItemRequest();
        getItemRequest.setTableName("Tickets");
        Map<String,AttributeValue>map=new HashMap<>();
        map.put("location_timings",new AttributeValue(locatioon_timings));
        getItemRequest.setKey(map);

        GetItemResult getItemResult = DynamodbClient.getClient().getItem(getItemRequest);
        Map<String,AttributeValue>response = getItemResult.getItem();
        AvailableTickets obj = DependencyInjection.getUtilClass().singleResponseTransformer(response , AvailableTickets.class);
        return obj.getSeatMatrix();
    }


    @Override
    public void setSeatMatrixInTickets(String locatioon_timings,int i,char ch) {

        UpdateItemRequest updateItemRequest=new UpdateItemRequest();
        updateItemRequest.setTableName("Tickets");

        String oldSeatMatrix  =   getSeatMatrixInTickets(locatioon_timings);

        String newSeatMatrix = oldSeatMatrix.substring(0,i) + ch + oldSeatMatrix.substring(i+1);

        Map<String,AttributeValue>values=new HashMap<>();
        values.put(":val" , new AttributeValue(newSeatMatrix));
        updateItemRequest.setExpressionAttributeValues(values);

        Map<String,String>names=new HashMap<>();
        names.put("#name","seatMatrix");
        updateItemRequest.setExpressionAttributeNames(names);

        Map<String,AttributeValue>map=new HashMap<>();
        map.put("location_timings" , new AttributeValue(locatioon_timings));
        updateItemRequest.setKey(map);

        updateItemRequest.setUpdateExpression("set #name = :val");

        DynamodbClient.getClient().updateItem(updateItemRequest);

    }

}
