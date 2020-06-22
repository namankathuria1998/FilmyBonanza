package src.com.filmybonanza.dynamodbClient;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;

public class DynamodbClient {

    private static AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient(new
            BasicAWSCredentials( "AKIAXCSP5TBAUU6W2BWD","w8D2sERuGcU4Rg3p0Tk/lGyyijYe58pZRQXX7MpG"));

    private DynamodbClient() {
    }

    public static AmazonDynamoDBAsyncClient getClient() {
        return client;
    }
}
