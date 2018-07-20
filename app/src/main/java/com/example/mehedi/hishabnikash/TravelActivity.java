package com.example.mehedi.hishabnikash;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class TravelActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    DbHelper dbHelper;
    private ListView listView;
    private Button btnAddHistory;
    View travelDialogView;

    Calendar calendar;
    int date;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // initializing the components
        init();

        // setting up the travel history list
        travelHistory();

    }

    /*
    * this method is responsible for initialising all the view components
    * @param void
    * @return void
    * */
    public void init () {
        dbHelper = new DbHelper(this);
        listView = findViewById(R.id.travelListView);
        btnAddHistory = findViewById(R.id.travelCostAdd);
    }

    /*
    * this method is responsible for setting up the list view for the travel history
    * @param void
    * @return void
    * */
    public void travelHistory () {
        btnAddHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.travelCostAdd) {
            LayoutInflater inflater = getLayoutInflater();
            travelDialogView = inflater.inflate(R.layout.travel_cost_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(travelDialogView);
            builder.setTitle("Add Travel History");
            builder.setPositiveButton("Add", (DialogInterface.OnClickListener) this);
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) this);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            EditText editTextSource = travelDialogView.findViewById(R.id.et_travelCostFrom);
            EditText editTextDestination = travelDialogView.findViewById(R.id.et_travelCostTo);
            EditText editTextVehicle = travelDialogView.findViewById(R.id.et_travelCostVehicle);
            EditText editTextAmount = travelDialogView.findViewById(R.id.et_travelCostAmount);

            String source = editTextSource.getText().toString();
            String destination = editTextDestination.getText().toString();
            String vehicle = editTextVehicle.getText().toString();
            int amount = Integer.parseInt(editTextAmount.getText().toString());

            Cursor dbCursor = dbHelper.checkSavingsPlan(month, year);

            // checking if any of the field is empty
            if (source.equals("") || destination.equals("") || vehicle.equals("") || amount == 0) {
                Toast.makeText(this, "Please fill up all the fields before you submit", Toast.LENGTH_LONG).show();
            } else {
                long id = dbHelper.addTravelHistory(new TravelHistoryModel(source, destination, vehicle, amount));
                if (id > 0) {
                    if (dbCursor.getCount() > 0) {
                        dbCursor.moveToFirst();
                        int existingAmount = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("ACTUAL_AMOUNT")));
                        amount += existingAmount;
                        dbHelper.updateSavingsPlanExpense(amount, month, year);
                    }
                    Toast.makeText(this, "Travel history added to the list", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add travel history", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (i == -2) {
            dialogInterface.cancel();
        }
    }
}
