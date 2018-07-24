package com.example.mehedi.hishabnikash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mehedi.hishabnikash.daily_cost.OthersCostActivity;
import com.example.mehedi.hishabnikash.pin_code.ChangePinActivity;
import com.example.mehedi.hishabnikash.pin_code.PinCodeActivity;
import com.example.mehedi.hishabnikash.saving_plan.SavingsPlanActivity;
import com.example.mehedi.hishabnikash.travel_cost.TravelActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVehicleCost, btnOtherBills, btnSavingsPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing the components
        init();

        // setting up the event listeners for the buttons
        setListeners();
    }

    public void init() {
        btnVehicleCost = findViewById(R.id.btn_vehicleCost);
        btnOtherBills = findViewById(R.id.btn_othersCost);
        btnSavingsPlan = findViewById(R.id.btn_savingsPlan);
    }

    public void setListeners() {
        btnVehicleCost.setOnClickListener(this);
        btnOtherBills.setOnClickListener(this);
        btnSavingsPlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_vehicleCost) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.credits) {
            startActivity(new Intent(this, CreditsActivity.class));
        } else if (item.getItemId() == R.id.set_pin) {
            startActivity(new Intent(this, PinCodeActivity.class));
        } else if (item.getItemId() == R.id.change_pin) {
            startActivity(new Intent(this, ChangePinActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}
