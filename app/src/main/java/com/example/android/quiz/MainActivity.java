package com.example.android.quiz;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;



public class MainActivity extends AppCompatActivity {
    private FrameLayout frame_layout;
    private BottomNavigationView bottom_nav;
    private CalendarFragment calendar_fragment;
    private NotificationFragment notification_fragment;
    private ProfileFragment profile_fragment;
    private AddEventFragment add_event_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default calendar page
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new CalendarFragment());
        fragmentTransaction.commit();

        frame_layout=(FrameLayout)findViewById(R.id.frame_layout);
        bottom_nav=(BottomNavigationView)findViewById(R.id.bottom_nav);
        calendar_fragment=new CalendarFragment();
       // notification_fragment=new NotificationFragment();
        profile_fragment=new ProfileFragment();
        add_event_fragment=new AddEventFragment();
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottom_nav_calendar:
                        //call calendar fragment

                        setFragment(calendar_fragment);
                        return true;
                   /* case R.id.bottom_nav_notification:
                        //call notification fragment
                        setFragment(notification_fragment);
                        return true;*/
                    case R.id.bottom_nav_profile:
                        //call profile fragment

                        setFragment(profile_fragment);
                        return true;
                    case R.id.bottom_nav_add_event:
                        //call profile fragment

                        setFragment(add_event_fragment);
                        return true;
                    default:

                        return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
