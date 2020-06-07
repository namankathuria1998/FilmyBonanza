package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.R;
import src.com.filmybonanza.UserDetails;
import src.com.filmybonanza.singleton.DependencyInjection;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.dynamodbv2.document.Item;
//import com.amazonaws.services.dynamodbv2.document.Table;

// the UserDetails will be displayed on this screen/activity
public class ShowUserDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_details);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final EditText name=findViewById(R.id.name);
        final EditText email=findViewById(R.id.email);
        final EditText phoneNo=findViewById(R.id.phoneNo);

        final Button edit=findViewById(R.id.btn);
        final Button save = findViewById(R.id.save);
        save.setEnabled(false);
        name.setEnabled(false);
        email.setEnabled(false);
        phoneNo.setEnabled(false);



//        MainActivity obj=new MainActivity();
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserDetails userDetails = DependencyInjection.getUserHandler()
                .getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), ShowUserDetails.this);

        name.setText(userDetails.getUserName());
        email.setText(userDetails.getUserEmail());
        phoneNo.setText(userDetails.getUserPhoneNo());


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setEnabled(true);
                name.setEnabled(true);
                email.setEnabled(true);
                phoneNo.setEnabled(true);

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String curname = name.getText().toString();
                String curemail = email.getText().toString();
                String curpassword = phoneNo.getText().toString();

                FirebaseAuth.getInstance().getCurrentUser().updateEmail(curemail);
                UserDetails newuserDetails=new UserDetails(curname,curemail,curpassword,FirebaseAuth.getInstance().getCurrentUser().getUid());
                DependencyInjection.getUserHandler().updateUserDetails(newuserDetails);

                save.setEnabled(false);
                name.setEnabled(false);
                email.setEnabled(false);
                phoneNo.setEnabled(false);

            }
        });


    }


}
