package com.example.mehedi.hishabnikash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) this);

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
            return 0;
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
}
