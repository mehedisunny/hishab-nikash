package com.example.mehedi.hishabnikash;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTongDokan, btnVehicleCost, btnOtherBills, btnSavingsPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializing the components
        init();

        // setting up the event listeners for the buttons
        setListeners();
    }

    public void init() {
        btnTongDokan = findViewById(R.id.btn_tongDokan);
        btnVehicleCost = findViewById(R.id.btn_vehicleCost);
        btnOtherBills = findViewById(R.id.btn_othersCost);
        btnSavingsPlan = findViewById(R.id.btn_savingsPlan);
    }

    public void setListeners() {
        btnTongDokan.setOnClickListener(this);
        btnVehicleCost.setOnClickListener(this);
        btnOtherBills.setOnClickListener(this);
        btnSavingsPlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_tongDokan) {

            // redirecting to te tong dokan activity
            startActivity(new Intent(this, TongDokanActivity.class));

        } else if (view.getId() == R.id.btn_vehicleCost) {

            // redirecting to te vehicle cost activity
            startActivity(new Intent(this, TravelActivity.class));

        } else if (view.getId() == R.id.btn_othersCost) {

            // redirecting to te other cost activity
            startActivity(new Intent(this, OthersCostActivity.class));

        } else if (view.getId() == R.id.btn_savingsPlan) {

            // redirecting to te savings plan activity
            startActivity(new Intent(this, SavingsPlanActivity.class));

        }

    }
}
