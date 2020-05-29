package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import src.com.filmybonanza.R;
import src.com.filmybonanza.UpcomingEvents;
import src.com.filmybonanza.UpcomingMovies;
import src.com.filmybonanza.UserDetails;
import src.com.filmybonanza.dynamodbClient.DynamodbClient;
import src.com.filmybonanza.singleton.DependencyInjection;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.dynamodbv2.document.Item;
//import com.amazonaws.services.dynamodbv2.document.Table;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager viewPager = findViewById(R.id.viewpag);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new myviewpageradapter(getSupportFragmentManager()));

        UserDetails userDetails = DependencyInjection.getUserHandler().getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(),this);

        if(userDetails==null)
        {
            PutItemRequest putItemRequest=new PutItemRequest();
            Map<String, AttributeValue> map=new HashMap<>();
            map.put("uid",new AttributeValue(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            map.put("userName",new AttributeValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
            map.put("userEmail",new AttributeValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            map.put("userPassword",new AttributeValue(""));

            putItemRequest.setTableName("UsersDetails");
            putItemRequest.setItem(map);
            DynamodbClient.getClient().putItem(putItemRequest);

//            PutItemRequest putItemRequest1=new PutItemRequest();
//            putItemRequest.setTableName("UsersBookings");
//
//            Map<String,AttributeValue>bookmap=new HashMap<>();
//            bookmap.put("uid",new AttributeValue(FirebaseAuth.getInstance().getCurrentUser().getUid()));
//            bookmap.put("bookedevents",new AttributeValue().withL());
        }

    }


    class myviewpageradapter extends FragmentPagerAdapter {

        public myviewpageradapter(@NonNull FragmentManager fm) {
            super(fm);
        }



        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Movies";
                case 1:
                    return "Events";

                default:return null;

            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new UpcomingMovies();
                case 1:
                    return new UpcomingEvents();
                default:return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




}
