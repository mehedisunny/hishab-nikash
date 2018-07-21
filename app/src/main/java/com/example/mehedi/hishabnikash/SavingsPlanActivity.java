package com.example.mehedi.hishabnikash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SavingsPlanActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private EditText editTextBudget;
    private Button btnSaveBudget;
    Calendar calendar;
    int year;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings_plan);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;

        // initializing components of the activity
        init();

        // set list view
        setListView();

        // setting up the listeners
        setListener();


    }

    public void init() {
        editTextBudget = findViewById(R.id.et_monthlyBudget);
        listView = findViewById(R.id.listViewSavingsPlan);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
    }

    public void setListView() {
        DbHelper dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.getAllSavingsPlan();
        SavingsAdapter savingsAdapter = new SavingsAdapter(this,cursor);
        listView.setAdapter(savingsAdapter);
    }

    public void clearFields () {
        editTextBudget.setText("");
    }

    public void setListener() {
        btnSaveBudget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSaveBudget) {
            String amount = editTextBudget.getText().toString();
            if (amount.equals("")) {
                Toast.makeText(this,"Please enter an amount",Toast.LENGTH_SHORT).show();
            } else {
                int targetAmount = Integer.parseInt(amount);
                DbHelper dbHelper = new DbHelper(this);
                Cursor cursor = dbHelper.checkSavingsPlan(month, year);
                if (cursor.getCount() > 0) {
                    dbHelper.updateSavingsPlan(new SavingsPlanHolder(targetAmount, month, year));
                    setListView();
                    clearFields();
                    Toast.makeText(this,"Target amount for this month is updated",Toast.LENGTH_SHORT).show();
                } else {
                    long id = dbHelper.addSavingsPlan(new SavingsPlanHolder(targetAmount,0,month,year));
                    clearFields();
                    if (id > 0) {
                        Toast.makeText(this,"Your target amount for this month is set to BDT "+targetAmount,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this,"Failed to set target amount",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    class SavingsAdapter extends BaseAdapter {

        Cursor cursor;
        LayoutInflater inflater;
        Context context;

        SavingsAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            this.context = context;
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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.saving_plan_layout, viewGroup, false);
            }

            while (cursor.moveToNext()) {
                TextView textViewMonth = view.findViewById(R.id.tv_savingsMonth);
                TextView textViewExpected = view.findViewById(R.id.tvExpectedBudget);
                TextView textViewEstimated = view.findViewById(R.id.tvEstimatedBudget);
                TextView textViewResult = view.findViewById(R.id.tvSavingsResult);

                int expectedAmount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("EXPECTED_AMOUNT")));
                int estimatedAmount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ACTUAL_AMOUNT")));
                int result = expectedAmount - estimatedAmount;

                if (result < 0) {
                    result = result * (-1);
                    textViewResult.setText("Result: \n"+result+" over");
                    textViewResult.setTextColor(getResources().getColor(R.color.colorError,null));
                } else {
                    textViewResult.setText("Result: \n"+result+" left");
                    textViewResult.setTextColor(getResources().getColor(R.color.colorSuccess,null));
                }

                textViewExpected.setText("Target: \n"+cursor.getString(cursor.getColumnIndex("EXPECTED_AMOUNT")));
                textViewEstimated.setText("Expense: \n"+cursor.getString(cursor.getColumnIndex("ACTUAL_AMOUNT")));
            }

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
