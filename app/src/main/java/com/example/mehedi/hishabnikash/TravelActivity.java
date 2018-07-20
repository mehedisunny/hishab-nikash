package com.example.mehedi.hishabnikash;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        // initializing the components
        init();

        // view action listeners
        listener();

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
    * this method is responsible for showing all the result for the current month
    * @param ArrayList<TravelHistoryModel> object
    * @return ArrayList<TravelHistoryModel> travelHistoryModels
    * */
    public void travelHistory () {
        ArrayList<TravelHistoryModel> travelHistoryModels = new ArrayList<>();

        travelHistoryModels = dbHelper.getTravelCostList(month, year);
        TravelCostAdapter travelCostAdapter = new TravelCostAdapter(this, travelHistoryModels);
        listView.setAdapter(travelCostAdapter);
    }

    /*
    * this method is responsible for setting up the list view for the travel history
    * @param void
    * @return void
    * */
    public void listener () {
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
            int calculatedAmount = 0;
            if (!editTextAmount.getText().toString().equals("")) {
                calculatedAmount = Integer.parseInt(editTextAmount.getText().toString());
            }
            int amount = calculatedAmount;

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
                    travelHistory();
                } else {
                    Toast.makeText(this, "Failed to add travel history", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (i == -2) {
            dialogInterface.cancel();
        }
    }

    class TravelCostAdapter extends BaseAdapter {

        ArrayList<TravelHistoryModel> travelCostList;
        LayoutInflater inflater;
        Context context;

        TravelCostAdapter(Context context, ArrayList<TravelHistoryModel> costHolder) {
            this.context = context;
            this.travelCostList = costHolder;
        }

        @Override
        public int getCount() {
            return travelCostList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.travel_cost_layout, viewGroup, false);
            }


            TextView textViewSource = view.findViewById(R.id.travel_costFrom);
            TextView textViewDestination = view.findViewById(R.id.travel_costTo);
            TextView textViewVehicle = view.findViewById(R.id.travel_costVehicle);
            TextView textViewAmount = view.findViewById(R.id.travel_costAmount);
            TextView textViewDate = view.findViewById(R.id.travel_costDate);

            TravelHistoryModel historyModel = travelCostList.get(i);

            String date = historyModel.getDate()+ "-"+ historyModel.getMonth()+ "-" + historyModel.getYear();

            textViewSource.setText(historyModel.getSourcePlace());
            textViewDestination.setText(historyModel.getDestinationPlace());
            textViewVehicle.setText(historyModel.getVehicleType());
            textViewAmount.setText(historyModel.getAmount()+" tk");
            textViewDate.setText(date);


            return view;
        }
    }
}
