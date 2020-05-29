package src.com.filmybonanza.dynamodbClient;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;

public class DynamodbClient {

    private static AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient(new
            BasicAWSCredentials( "ACCESS_KEY","SECRET_KEY"));

    private DynamodbClient() {
    }

    public static AmazonDynamoDBAsyncClient getClient() {
        return client;
    }
}
