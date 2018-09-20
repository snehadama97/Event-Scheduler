package com.example.android.quiz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class add_new_event extends AppCompatActivity {
    private static String cardName = "";
    EditText editTextName, details;
    Button button;
    Spinner spinner;

    ListView listViewEvents;
    List<Event> eventList;
    RelativeLayout date, time;
    protected static TextView dateD, timeD;
    int day, month, year, hour, min;

    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        editTextName = (EditText) findViewById(R.id.editTextName);
        details = (EditText) findViewById(R.id.details);
        date = (RelativeLayout) findViewById(R.id.date);
        time = (RelativeLayout) findViewById(R.id.time);
        dateD = (TextView) findViewById(R.id.set_date);
        timeD = (TextView) findViewById(R.id.set_time);
        button = (Button) findViewById(R.id.button);
        spinner=(Spinner)findViewById(R.id.year);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });
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
            dateD.setText(  String.valueOf(day)+ "/" + String.valueOf(month) + "/" + String.valueOf(year));
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

    private void addEvent() {
       //getting the values to save
        String name = editTextName.getText().toString().trim();
        String detaiL=details.getText().toString().trim();
        String datE=dateD.getText().toString().trim();
        String timE=timeD.getText().toString().trim();
        String temp = spinner.getSelectedItem().toString();
        //    cardName= temp;

        databaseEvents = FirebaseDatabase.getInstance().getReference(temp);

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            String id= databaseEvents.push().getKey();


            Event event=new Event(id,name,detaiL,datE,timE);

            databaseEvents.child(id).setValue(event);

            Toast.makeText(this, "Event Added", Toast.LENGTH_LONG).show();

        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    //    Toast.makeText(this, "in button", Toast.LENGTH_LONG).show();
}
