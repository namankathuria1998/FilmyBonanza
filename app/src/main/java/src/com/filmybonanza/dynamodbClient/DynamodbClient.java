package src.com.filmybonanza.dynamodbClient;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;

public class DynamodbClient {

    private static AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient(new
            BasicAWSCredentials( "AKIAXCSP5TBARZFOQKM4","A9wldPWa26n8vNnjodknMPDGeFRb68XRWhepL4MY"));

    private DynamodbClient() {
    }

    public static AmazonDynamoDBAsyncClient getClient() {
        return client;
    }
}
