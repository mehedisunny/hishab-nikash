package com.example.mehedi.hishabnikash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, final long l) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TravelActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.travel_cost_dialog, null);

                ArrayList<TravelHistoryModel> singleData = new ArrayList<>();
                singleData = dbHelper.getSingleTravelCostInfo(l);

                final EditText editTextSource = view.findViewById(R.id.et_travelCostFrom);
                final EditText editTextDestination = view.findViewById(R.id.et_travelCostTo);
                final EditText editTextVehicleType = view.findViewById(R.id.et_travelCostVehicle);
                final EditText editTextAmount = view.findViewById(R.id.et_travelCostAmount);

                editTextSource.setText(singleData.get(0).getSourcePlace());
                editTextDestination.setText(singleData.get(0).getDestinationPlace());
                editTextVehicleType.setText(singleData.get(0).getVehicleType());
                editTextAmount.setText(singleData.get(0).getAmount()+"");

                final int currentBalance = Integer.parseInt(singleData.get(0).getAmount()+"");

                builder.setView(view);

                final Cursor dbCursor = dbHelper.checkSavingsPlan(month, year);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String source = editTextSource.getText().toString().trim();
                        final String destination = editTextDestination.getText().toString().trim();
                        final String vehicle = editTextVehicleType.getText().toString().trim();
                        int amount  = Integer.parseInt(editTextAmount.getText().toString().trim());
                        if (source.isEmpty() || destination.isEmpty() || vehicle.isEmpty() || amount == 0) {
                            Toast.makeText(TravelActivity.this, "Make sure you filled up all the fields", Toast.LENGTH_LONG).show();
                        } else {
                            dbHelper.updateTravelHistory(new TravelHistoryModel(source, destination, vehicle, amount), l);
                            if (dbCursor.getCount() > 0) {
                                int finalAmount = 0;
                                int updatedAmount = 0;
                                dbCursor.moveToFirst();
                                int existingAmount = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("ACTUAL_AMOUNT")));
                                if (currentBalance < amount) {
                                    updatedAmount = amount - currentBalance;
                                    finalAmount = existingAmount + updatedAmount;
                                } else {
                                    updatedAmount = currentBalance - amount;
                                    finalAmount = existingAmount - updatedAmount;
                                }

                                if (finalAmount <= 0) {
                                    finalAmount = 0;
                                }

                                dbHelper.updateSavingsPlanExpense(finalAmount, month, year);
                            }
                            Toast.makeText(TravelActivity.this, "Travel cost updated", Toast.LENGTH_LONG).show();
                            travelHistory();

                        }
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder travelAlertBuilder = new AlertDialog.Builder(TravelActivity.this);
                        travelAlertBuilder.setTitle("Are you sure to delete ?");
                        travelAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteTravelHistory(l);
                                if (dbCursor.getCount() > 0) {
                                    dbCursor.moveToFirst();
                                    int amount  = Integer.parseInt(editTextAmount.getText().toString());
                                    int existingAmount = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("ACTUAL_AMOUNT")));
                                    amount -= existingAmount;
                                    if (amount < 0) {
                                        amount = 0;
                                    }
                                    dbHelper.updateSavingsPlanExpense(amount, month, year);
                                }
                                Toast.makeText(TravelActivity.this, "Travel cost deleted", Toast.LENGTH_LONG).show();
                                travelHistory();
                            }
                        });

                        travelAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog travelAlert = travelAlertBuilder.create();
                        travelAlert.show();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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
            builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) this);

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

            String source = editTextSource.getText().toString().trim();
            String destination = editTextDestination.getText().toString().trim();
            String vehicle = editTextVehicle.getText().toString().trim();
            int calculatedAmount = 0;
            if (!editTextAmount.getText().toString().isEmpty()) {
                calculatedAmount = Integer.parseInt(editTextAmount.getText().toString().trim());
            }
            int amount = calculatedAmount;

            Cursor dbCursor = dbHelper.checkSavingsPlan(month, year);

            // checking if any of the field is empty
            if (source.isEmpty() || destination.isEmpty() || vehicle.isEmpty() || amount == 0) {
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
            return travelCostList.get(i).getId();
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
            finish();
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
            finish();
        } else if (item.getItemId() == R.id.credits) {
            startActivity(new Intent(this, CreditsActivity.class));
            finish();
        } else if (item.getItemId() == R.id.set_pin) {
            startActivity(new Intent(this, PinCodeActivity.class));
            finish();
        } else if (item.getItemId() == R.id.change_pin) {
            startActivity(new Intent(this, ChangePinActivity.class));
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
