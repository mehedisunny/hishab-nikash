package com.example.mehedi.hishabnikash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PinCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPinCode;
    private Button btnSetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        init();

        listeners();
    }

    public void init () {
        etPinCode = findViewById(R.id.et_pinCodeSet);
        btnSetCode = findViewById(R.id.btn_pinCodeSet);
    }

    public void listeners() {
        btnSetCode.setOnClickListener(this);
    }

    public void clearFields () {
        etPinCode.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pinCodeSet) {

            SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
            String isPinSet = sharedPreferences.getString("pin","hello");
            if (isPinSet.equals("hello")) {
                String pinCode = etPinCode.getText().toString().trim();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pin", pinCode);
                editor.commit();
                clearFields();
                Toast.makeText(this, "You have successfully set your pin", Toast.LENGTH_SHORT).show();
            } else {
                clearFields();
                Toast.makeText(this, "You have already set your pin code.", Toast.LENGTH_SHORT).show();
            }
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
