package com.example.mehedi.hishabnikash;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class OthersCostActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private ListView listView;
    private String[] months = {"january, 2018", "february,2018"};
    TextView purposeHeading, purposeAmount, purposeDate;
    Button btnAddCost;
    View dialogView;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_cost);

        init();

        setListView();

        setListener();
    }

    public void init() {
        listView = findViewById(R.id.otherCostListView);
        purposeHeading = findViewById(R.id.otherCostDescHeading);
        purposeAmount = findViewById(R.id.otherCostAmountHeading);
        purposeDate = findViewById(R.id.otherCostDateHeading);
        btnAddCost = findViewById(R.id.otherCostAddAmount);
        dbHelper = new DbHelper(this);
    }

    public void setListView() {
        Cursor cursor = dbHelper.getAllOthersCost();
        OthersCostAdapter savingsAdapter = new OthersCostAdapter(this, cursor);
        listView.setAdapter(savingsAdapter);
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

            if (costPurpose.equals("") || costAmount.equals("")) {
                Toast.makeText(this,"Please enter purpose of cost and amount", Toast.LENGTH_LONG).show();
            } else {
                DbHelper dbHelper = new DbHelper(this);
                long id = dbHelper.addOtherCost(new OtherCostHolder(costPurpose, Integer.parseInt(costAmount)));
                if (id > 0) {
                    Toast.makeText(this,"Your purpose and cost added to the list", Toast.LENGTH_LONG).show();
                    setListView();
                } else {
                    Toast.makeText(this,"Failed to add other cost", Toast.LENGTH_LONG).show();
                }
            }

        } else if (i == -2) {
            dialogInterface.cancel();
        }
    }

    class OthersCostAdapter extends BaseAdapter {

        Cursor cursor;
        LayoutInflater inflater;
        Context context;

        OthersCostAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
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

            while (cursor.moveToNext()) {
                TextView textViewPurpose = view.findViewById(R.id.otherCostDesc);
                TextView textViewAmount = view.findViewById(R.id.otherCostAmount);
                TextView textViewDate = view.findViewById(R.id.otherCostDate);

                textViewPurpose.setText(cursor.getString(cursor.getColumnIndex("PURPOSE")));
                textViewAmount.setText(cursor.getString(cursor.getColumnIndex("AMOUNT")));
            }

            return view;
        }
    }
}
