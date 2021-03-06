package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import src.com.filmybonanza.R;
import src.com.filmybonanza.model.UserDetails;
import src.com.filmybonanza.singleton.DependencyInjection;

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

        UserDetails userDetails = DependencyInjection.getUserHandler()
                .getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(),ShowUserDetails.this);

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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ShowUserDetails.this, "The email updated.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                UserDetails newuserDetails=new UserDetails(curname,curemail,curpassword,FirebaseAuth.getInstance().getCurrentUser().getUid());
                DependencyInjection.getUserHandler().updateUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(),newuserDetails);

                save.setEnabled(false);
                name.setEnabled(false);
                email.setEnabled(false);
                phoneNo.setEnabled(false);
            }
        });
    }
}
