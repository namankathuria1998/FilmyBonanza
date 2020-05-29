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

import src.com.filmybonanza.BookedEvent;
import src.com.filmybonanza.Event;
import src.com.filmybonanza.R;
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
//        names.put("#password","userPassword");
        updateItemRequest.setExpressionAttributeNames(names);

        Map<String, AttributeValue>values=new HashMap<>();
        values.put(":val1",new AttributeValue(newUserDetails.getUserName()));
        values.put(":val2",new AttributeValue(newUserDetails.getUserEmail()));
//        values.put(":val3",new AttributeValue(newUserDetails.getUserPassword()));
        updateItemRequest.setExpressionAttributeValues(values);

        updateItemRequest.setUpdateExpression("set #email = :val2 ,#name = :val1");

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
        return null;
    }
}
