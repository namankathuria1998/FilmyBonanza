package src.com.filmybonanza.activities;

import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import src.com.filmybonanza.R;
import src.com.filmybonanza.fragments.UpcomingEvents;
import src.com.filmybonanza.fragments.UpcomingMovies;
import src.com.filmybonanza.model.UserDetails;
import src.com.filmybonanza.singleton.DependencyInjection;

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
            DependencyInjection.getUserHandler().putNewUserintoDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid()
            ,FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
            ,FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
