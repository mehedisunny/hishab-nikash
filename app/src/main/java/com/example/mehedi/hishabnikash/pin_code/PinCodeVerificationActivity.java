package com.example.mehedi.hishabnikash.pin_code;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mehedi.hishabnikash.MainActivity;
import com.example.mehedi.hishabnikash.R;

public class PinCodeVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPinCode;
    private Button btnSetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_verification);

        init();

        listeners();
    }

    public void init () {
        etPinCode = findViewById(R.id.et_pinCodeCheck);
        btnSetCode = findViewById(R.id.btn_pinCodeCheck);
    }

    public void listeners() {
        btnSetCode.setOnClickListener(this);
    }

    public void clearFields () {
        etPinCode.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pinCodeCheck) {

            SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
            String isPinSet = sharedPreferences.getString("pin","hello");
            String userInput = etPinCode.getText().toString();
            if (isPinSet.equals(userInput)) {
                startActivity(new Intent(PinCodeVerificationActivity.this, MainActivity.class));
                finish();
            } else {
                clearFields();
                Toast.makeText(this, "Sorry!! Wrong pin number", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
