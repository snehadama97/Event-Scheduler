package com.example.android.quiz;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    public static MyDynamicCalendar myCalendar;
    private TextView text;
    //private CalendarView calender;
    DatabaseReference databaseEvents;
    private StickySwitch weeklySchedule;
    public CalendarFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* calender=(CalendarView)getView().findViewById(R.id.calendarView);
        calender.showCurrentMonthPage();*/
       // text=getView().findViewById(R.id.text);
        myCalendar=getView().findViewById(R.id.myCalendar);
        initializeCalendar();



       // myCalendar.showMonthViewWithBelowEvents();
        myCalendar.showMonthView();

        databaseEvents = FirebaseDatabase.getInstance().getReference("FY");


        StickySwitch stickySwitch = (StickySwitch) getView().findViewById(R.id.monthlySchedule);
        stickySwitch.setDirection(StickySwitch.Direction.RIGHT,false);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);

                Fragment TargetFragment=new WeeklyScheduleFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

       /* weeklySchedule=(Button)getView().findViewById(R.id.week);
        weeklySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment TargetFragment=new WeeklyScheduleFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/

        // date click listener
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(final Date date) {
                myCalendar.getEventList(new GetEventListListener() {
                    @Override
                    public void eventList(ArrayList<EventModel> eventList) {
                        HashSet<String> set=new HashSet<>();
                        for (int i = 0; i < eventList.size(); i++) {
                           // str+=eventList.get(i).getStrName()+" ";

                            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                            String dt1=format1.format(date);
                            if(eventList.get(i).getStrDate().equals(dt1)){
                                set.add("Event Name: "+eventList.get(i).getStrName()+"\nEvent Time: "+eventList.get(i).getStrStartTime());
                            }
                           // text.setText(eventList.get(i).getStrStartTime());
                        }

                        String result="";
                        for(String s:set){
                            result+=s+"\n\n";
                        }

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());


                        alertDialogBuilder.setMessage(result);

                        alertDialogBuilder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface arg0, int arg1) {

                             }
                         });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                       // text.setText(result);
                    }
                });
            }

            @Override
            public void onLongClick(Date date) {

            }
        });

        super.onStart();
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //String date=event.getEventDate();
                    String[] arr=event.getEventDate().toString().split("/");
                    //adding artist to the list
                    myCalendar.addEvent(arr[0]+"-"+arr[1]+"-"+arr[2],event.getEventTime(),event.getEventTime(),event.getEventName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }



    public void initializeCalendar(){

       // myCalendar.setCalendarBackgroundColor("#eeeeee");
        myCalendar.setHeaderBackgroundColor("#21bcce");
        myCalendar.setHeaderTextColor("#ffffff");
        myCalendar.setNextPreviousIndicatorColor("#245675");
        myCalendar.setWeekDayLayoutBackgroundColor("#ffffff");
        myCalendar.setWeekDayLayoutTextColor("#246245");
        myCalendar.setExtraDatesOfMonthBackgroundColor("#b3b3b3");
        myCalendar.setExtraDatesOfMonthTextColor("#756325");
        myCalendar.setDatesOfMonthBackgroundColor("#ffffff");
        myCalendar.setDatesOfMonthTextColor("#745632");
        myCalendar.setCurrentDateBackgroundColor("#21bcce");
        myCalendar.setCurrentDateTextColor("#ffffff");
        myCalendar.setBelowMonthEventTextColor("#425684");
        myCalendar.setBelowMonthEventDividerColor("#635478");
    }
}
