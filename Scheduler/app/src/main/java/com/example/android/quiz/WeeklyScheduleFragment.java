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
import android.widget.ToggleButton;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyScheduleFragment extends Fragment {
    DatabaseReference databaseEvents;
    public static MyDynamicCalendar myCalendar1;
    private TextView text;
    private StickySwitch monthlySchedule;
    public WeeklyScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_schedule, container, false);

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* calender=(CalendarView)getView().findViewById(R.id.calendarView);
        calender.showCurrentMonthPage();*/
        // text=getView().findViewById(R.id.text);
        myCalendar1=getView().findViewById(R.id.myCalendar1);
        myCalendar1.deleteAllEvent();

        initializeCalendar();
        StickySwitch stickySwitch = (StickySwitch) getView().findViewById(R.id.weeklySchedule);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
                Fragment TargetFragment=new CalendarFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
      /*  monthlySchedule=(ToggleButton)getView().findViewById(R.id.monthlySchedule);
        monthlySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment TargetFragment=new CalendarFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/

        // myCalendar1.showMonthViewWithBelowEvents();
        myCalendar1.showAgendaView();

        databaseEvents = FirebaseDatabase.getInstance().getReference("FY");




        // date click listener
        myCalendar1.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(final Date date) {
                myCalendar1.getEventList(new GetEventListListener() {
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
                    myCalendar1.addEvent(arr[0]+"-"+arr[1]+"-"+arr[2],event.getEventTime(),event.getEventTime(),event.getEventName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }



    public void initializeCalendar(){

        // myCalendar1.setCalendarBackgroundColor("#eeeeee");
        myCalendar1.setHeaderBackgroundColor("#21bcce");
        myCalendar1.setHeaderTextColor("#ffffff");
        myCalendar1.setNextPreviousIndicatorColor("#245675");
        myCalendar1.setWeekDayLayoutBackgroundColor("#ffffff");
        myCalendar1.setWeekDayLayoutTextColor("#246245");
        myCalendar1.setExtraDatesOfMonthBackgroundColor("#b3b3b3");
        myCalendar1.setExtraDatesOfMonthTextColor("#756325");
        myCalendar1.setDatesOfMonthBackgroundColor("#ffffff");
        myCalendar1.setDatesOfMonthTextColor("#745632");
        myCalendar1.setCurrentDateBackgroundColor("#21bcce");
        myCalendar1.setCurrentDateTextColor("#ffffff");
        myCalendar1.setBelowMonthEventTextColor("#425684");
        myCalendar1.setBelowMonthEventDividerColor("#635478");
    }

}
