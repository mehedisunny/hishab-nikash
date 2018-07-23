package com.example.mehedi.hishabnikash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class OthersCostActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private ListView othersCostListView;
    TextView purposeHeading, purposeAmount, purposeDate;
    Button btnAddCost;
    View dialogView;
    DbHelper dbHelper;
    Calendar calendar;
    int year;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_cost);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;

        init();

        setOtherCostList();

        setListener();
    }

    public void init() {
        othersCostListView = findViewById(R.id.otherCostListView);
        purposeHeading = findViewById(R.id.otherCostDescHeading);
        purposeAmount = findViewById(R.id.otherCostAmountHeading);
        purposeDate = findViewById(R.id.otherCostDateHeading);
        btnAddCost = findViewById(R.id.otherCostAddAmount);
        dbHelper = new DbHelper(this);
    }

    public void setOtherCostList() {
        ArrayList<OtherCostHolder> costList = dbHelper.getAllOthersCost(month, year);
        OthersCostAdapter savingsAdapter = new OthersCostAdapter(this, costList);
        othersCostListView.setAdapter(savingsAdapter);
        othersCostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OthersCostActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.other_cost_dialog, null);

                ArrayList<OtherCostHolder> singleData = new ArrayList<>();
                singleData = dbHelper.getSingleCostInfo(l);

                final EditText editTextPurpose = view.findViewById(R.id.et_otherCostPurpose);
                final EditText editTextAmount = view.findViewById(R.id.et_otherCostAmount);

                editTextPurpose.setText(singleData.get(0).getPurpose());
                editTextAmount.setText(singleData.get(0).getAmount()+"");

                final int currentBalance = Integer.parseInt(singleData.get(0).getAmount()+"");

                builder.setView(view);

                final Cursor dbCursor = dbHelper.checkSavingsPlan(month, year);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String purpose = editTextPurpose.getText().toString();
                        int amount  = Integer.parseInt(editTextAmount.getText().toString());
                        dbHelper.updateOthersCost(new OtherCostHolder(purpose, amount), l);
                        if (dbCursor.getCount() > 0) {
                            int finalAmount = 0;
                            int updatedAmount = 0;
                            dbCursor.moveToFirst();
                            int existingAmount = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("ACTUAL_AMOUNT")));
                            if (currentBalance < amount) {
                                updatedAmount = amount - currentBalance;
                                finalAmount = existingAmount + updatedAmount;
                                amount += existingAmount;
                            } else {
                                updatedAmount = currentBalance - amount;
                                finalAmount = existingAmount - updatedAmount;
                            }

                            if (finalAmount <= 0) {
                                finalAmount = 0;
                            }

                            dbHelper.updateSavingsPlanExpense(finalAmount, month, year);
                        }
                        Toast.makeText(OthersCostActivity.this, "Daily cost updated", Toast.LENGTH_LONG).show();
                        setOtherCostList();
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteOtherCost(l);
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
                        Toast.makeText(OthersCostActivity.this, "Daily cost deleted", Toast.LENGTH_LONG).show();
                        setOtherCostList();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    public void setListener () {
        btnAddCost.setOnClickListener(this);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.otherCostAddAmount) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            dialogView = inflater.inflate(R.layout.other_cost_dialog, null);
            builder.setView(dialogView);

            builder.setTitle("Add Other Cost");
            builder.setPositiveButton("Add", (DialogInterface.OnClickListener) this);
            builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) this);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            EditText purpose = dialogView.findViewById(R.id.et_otherCostPurpose);
            EditText amount = dialogView.findViewById(R.id.et_otherCostAmount);

            String costPurpose = purpose.getText().toString();
            String costAmount = amount.getText().toString();
            Cursor dbCursor = dbHelper.checkSavingsPlan(month, year);

            int convertedAmount = 0;
           if (!costAmount.equals("")) {
               convertedAmount = Integer.parseInt(costAmount);
           }

            if (costPurpose.equals("") || costAmount.equals("")) {
                Toast.makeText(this,"Please enter purpose of cost and amount", Toast.LENGTH_LONG).show();
            } else {
                DbHelper dbHelper = new DbHelper(this);
                long id = dbHelper.addOtherCost(new OtherCostHolder(costPurpose, convertedAmount));
                if (id > 0) {
                    if (dbCursor.getCount() > 0) {
                        dbCursor.moveToFirst();
                        int existingAmount = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("ACTUAL_AMOUNT")));
                        convertedAmount += existingAmount;
                        dbHelper.updateSavingsPlanExpense(convertedAmount, month, year);
                    }
                    Toast.makeText(this,"Your purpose and cost added to the list", Toast.LENGTH_LONG).show();
                    setOtherCostList();
                } else {
                    Toast.makeText(this,"Failed to add other cost", Toast.LENGTH_LONG).show();
                }
            }

        } else if (i == -2) {
            dialogInterface.cancel();
        }
    }

    class OthersCostAdapter extends BaseAdapter {

        ArrayList<OtherCostHolder> costHolder;
        LayoutInflater inflater;
        Context context;

        OthersCostAdapter(Context context, ArrayList<OtherCostHolder> costHolder) {
            this.context = context;
            this.costHolder = costHolder;
        }

        @Override
        public int getCount() {
            return this.costHolder.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return costHolder.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.others_cost_layout, viewGroup, false);
            }


            TextView textViewPurpose = view.findViewById(R.id.otherCostDesc);
            TextView textViewAmount = view.findViewById(R.id.otherCostAmount);
            TextView textViewDate = view.findViewById(R.id.otherCostDate);

            OtherCostHolder otherCostHolder = costHolder.get(i);

            String date = otherCostHolder.getDate()+ "-"+ otherCostHolder.getMonth()+ "-" + otherCostHolder.getYear();

            textViewPurpose.setText(otherCostHolder.getPurpose());
            textViewAmount.setText(otherCostHolder.getAmount()+" tk");
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
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.credits) {
            startActivity(new Intent(this, CreditsActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}
