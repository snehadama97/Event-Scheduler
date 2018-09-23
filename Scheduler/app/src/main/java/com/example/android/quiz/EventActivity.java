package com.example.android.quiz;

import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.android.quiz.AddEventFragment.getCard;
import static com.example.android.quiz.CalendarFragment.myCalendar;
//import static com.example.android.quiz.CalendarFragment.myCalendar;

//from mainActivity to here, shows list of events
public class EventActivity extends AppCompatActivity {
    private static String eventD, eventT, eventDate, eventName, eventID,cardName = "";
    DatabaseReference databaseEvents;

    ImageView del;
    TextView name;
    EditText editTextName, details;
    RelativeLayout date, time;
    Button buttonUpdate;
    protected static TextView dateD, timeD;
    int day, month, year,hour, min ;

    ListView listViewEvents;
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        cardName = AddEventFragment.getCard();
        //    Toast.makeText(this, "We are phone"+cardName, Toast.LENGTH_LONG).show();



        databaseEvents = FirebaseDatabase.getInstance().getReference(cardName);
        listViewEvents = (ListView) findViewById(R.id.listView);
        eventList=new ArrayList<>();

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Event event = eventList.get(i);
                eventID=event.getEventId();
                eventName=event.getEventName();
                eventD=event.getEventDetails();
                eventT=event.getEventTime();
                eventDate=event.getEventDate();
                showUpdateDeleteDialog(event.getEventId(), event.getEventName());

            }
        });



    }


    private void showUpdateDeleteDialog(String eventId, String eventName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.event_dialog, null);
        del=(ImageView)dialogView.findViewById(R.id.delete);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
                b.dismiss();
            }
        });
    }


    private void deleteEvent() {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(cardName).child(eventID);
        dR.removeValue();

        Toast.makeText(this, "Event Deleted", Toast.LENGTH_LONG).show();
        myCalendar.deleteAllEvent();
    }

    private void toast() {
        Toast.makeText(this, "We are in "+eventID, Toast.LENGTH_LONG).show();

    }

    public void updateEvent(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_event, null);

        name=(TextView)dialogView.findViewById(R.id.editTextName);
        name.setText(eventName);

        final EditText Udetails = (EditText) dialogView.findViewById(R.id.details);
        Udetails.setText(eventD);
        final TextView Udate=(TextView) dialogView.findViewById(R.id.set_date);
        Udate.setText(eventDate);
        final TextView Utime=(TextView) dialogView.findViewById(R.id.set_time);
        Utime.setText(eventT);



        dateD = (TextView) dialogView.findViewById(R.id.set_date);
        timeD = (TextView) dialogView.findViewById(R.id.set_time);
        buttonUpdate = (Button) dialogView.findViewById(R.id.button);
        date=(RelativeLayout) dialogView.findViewById(R.id.date);
        time=(RelativeLayout) dialogView.findViewById(R.id.time);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Select time");
            }
        });


        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String detaiL=Udetails.getText().toString().trim();
                String datE=dateD.getText().toString().trim();
                String timE=timeD.getText().toString().trim();
                upDate(eventID, eventName,detaiL,datE,timE);
                b.dismiss();

            }
        });
        //  upDate() send parameters

    }

    private boolean upDate(String eventID, String eventName, String detaiL, String datE, String timE) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(cardName).child(eventID);

        Event event= new Event(eventID,eventName,detaiL,datE,timE);
        dR.setValue(event);
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    eventList.add(event);
                }
                EventList eventAdapter =new EventList(EventActivity.this, eventList);
                listViewEvents.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addNewEvent(View view) {
        Intent myIntent = new Intent(EventActivity.this, add_new_event.class);
        startActivity(myIntent);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month=month+1;
            if(month==13){
                year++;
                month=1;
            }
            dateD.setText(String.valueOf(day)+ "/" + String.valueOf(month) + "/" + String.valueOf(year));
        }
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            timeD.setText(String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
        }
    }



    //      DatabaseReference dR = FirebaseDatabase.getInstance().getReference(cardName).child(eventID);
    //    dR.removeValue();


    // Toast.makeText(this, "Done Long Click", Toast.LENGTH_LONG).show();
    /*    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_event_dialog, null);

        editTextName = (EditText) findViewById(R.id.editTextName);
        details=(EditText) findViewById(R.id.details);
        date=(RelativeLayout) findViewById(R.id.date);
        time=(RelativeLayout) findViewById(R.id.time);

        dateD=(TextView)findViewById(R.id.date_text);
        timeD=(TextView)findViewById(R.id.set_time);


        details.setText("um");

        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void toast() {
        Toast.makeText(this, "Done Click", Toast.LENGTH_SHORT).show();

    }


    public void addDate(View view) {
        DatePickerFragment mDatePicker = new DatePickerFragment();
        mDatePicker.show(getFragmentManager(), "Select date");
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {

            newDate=(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
        }
    }
*/
}