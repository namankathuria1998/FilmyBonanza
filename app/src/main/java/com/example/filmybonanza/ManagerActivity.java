package com.example.filmybonanza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Item item=new Item()
                .withPrimaryKey("event_id","123")
                .withString("Type","Movie")
                .withString("Title","Radhe")
                .withString("Image_url","https://smedia2.intoday.in/aajtak/images/stories/102018/bazaar_1024_1540492831_618x347.jpeg");

        Table table= dynamoDB.getTable("Events");
        table.putItem(item);


    }
}
