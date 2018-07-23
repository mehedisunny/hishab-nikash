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

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPinCodeCurrent, etPinCodeNew;
    private Button btnSetCode;
    private CheckBox cbRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        init();

        listeners();
    }

    public void init () {
        etPinCodeCurrent = findViewById(R.id.et_pinCodePrevious);
        etPinCodeNew = findViewById(R.id.et_pinCodeNew);
        btnSetCode = findViewById(R.id.btn_pinCodeReset);
        cbRemove = findViewById(R.id.cb_pinCodeRemove);
    }

    public void listeners() {
        btnSetCode.setOnClickListener(this);
        cbRemove.setOnClickListener(this);
    }

    public void clearFields () {
        etPinCodeCurrent.setText("");
        etPinCodeNew.setText("");
        cbRemove.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pinCodeReset) {
            SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
            String isPinSet = sharedPreferences.getString("pin","hello");
            String userInput = etPinCodeCurrent.getText().toString().trim();
            if (isPinSet.equals(userInput)) {
                if (cbRemove.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pin", "hello");
                    editor.commit();
                    Toast.makeText(this, "Your pin has successfully removed", Toast.LENGTH_SHORT).show();
                } else {
                    String pinCode = etPinCodeNew.getText().toString().trim();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pin", pinCode);
                    editor.commit();
                    Toast.makeText(this, "You have successfully set your new pin", Toast.LENGTH_SHORT).show();
                }

                clearFields();
                finish();
            } else {
                if (cbRemove.isChecked()) {
                    Toast.makeText(this, "Enter current pin before you remove it", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sorry current pin doesn't match with the stored one", Toast.LENGTH_SHORT).show();
                }
                clearFields();
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
