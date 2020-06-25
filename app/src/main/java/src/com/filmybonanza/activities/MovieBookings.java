package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import src.com.filmybonanza.model.BookedEvent;
import src.com.filmybonanza.views.BookingHistoryAdapter;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;

public class MovieBookings extends AppCompatActivity {

    ArrayList<BookedEvent>arrayList = new ArrayList<>();
    BookingHistoryAdapter adapter;
    RecyclerView recyclerView;

    int totalBookedMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recyclerView = findViewById(R.id.bookingrecycle);
        adapter = new BookingHistoryAdapter(arrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ArrayList<BookedEvent>resarrayList = DependencyInjection.getUserHandler().getUserBookings(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(resarrayList == null)
        {
            Log.e("chal ja bhai", "result" );
        }
        arrayList.clear();   arrayList.addAll(resarrayList);
        adapter.notifyDataSetChanged();
    }

}
