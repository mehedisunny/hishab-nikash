package com.example.mehedi.hishabnikash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPinCodeCurrent, etPinCodeNew;
    private Button btnSetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        init();

        listeners();
    }

    public void init () {
        etPinCodeCurrent = findViewById(R.id.et_pinCodePrevious);
        etPinCodeNew = findViewById(R.id.et_pinCodeNew);
        btnSetCode = findViewById(R.id.btn_pinCodeReset);
    }

    public void listeners() {
        btnSetCode.setOnClickListener(this);
    }

    public void clearFields () {
        etPinCodeCurrent.setText("");
        etPinCodeNew.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pinCodeReset) {
            SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
            String isPinSet = sharedPreferences.getString("pin","hello");
            String userInput = etPinCodeCurrent.getText().toString();
            if (isPinSet.equals(userInput)) {
                String pinCode = etPinCodeNew.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pin", pinCode);
                editor.commit();
                clearFields();
                Toast.makeText(this, "You have successfully set your new pin", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                clearFields();
                Toast.makeText(this, "Sorry current pin doesn't match with the stored one", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
