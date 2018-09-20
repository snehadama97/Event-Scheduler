package com.example.android.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.applandeo.materialcalendarview.CalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements View.OnClickListener {

    public static String phoneNumber = "9898989898";
    public static String cardName;
    CardView firstY,secondY,thirdY,lastY, facultY;

    DatabaseReference databaseEvents;

    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseEvents = FirebaseDatabase.getInstance().getReference("now");
        databaseEvents.setValue("babluu");

        firstY=(CardView)getView().findViewById(R.id.fy);
        secondY=(CardView)getView().findViewById(R.id.sy);
        thirdY=(CardView)getView().findViewById(R.id.ty);
        lastY=(CardView)getView().findViewById(R.id.ly);
        facultY=(CardView)getView().findViewById(R.id.faculty);

        firstY.setOnClickListener(this);
        secondY.setOnClickListener(this);
        thirdY.setOnClickListener(this);
        lastY.setOnClickListener(this);
        facultY.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        //detects which card was clicked as sends "EventActivity" the name of the card
        if(v==firstY) cardName= "FY";

        if(v==secondY) cardName= "SY";

        if(v==thirdY) cardName= "TY";

        if(v==lastY) cardName= "LY";

        if(v==facultY) cardName= "Faculty";

        Intent myIntent = new Intent(getActivity().getApplication(), EventActivity.class);
        startActivity(myIntent);
    }

    public static String getCard()
    {
        return cardName;
    }
}
