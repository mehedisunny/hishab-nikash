package com.example.mehedi.hishabnikash;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PinCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPinCode;
    private Button btnSetCode;
    private CheckBox cbRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        init();

        listeners();
    }

    public void init () {
        etPinCode = findViewById(R.id.et_pinCodeSet);
        btnSetCode = findViewById(R.id.btn_pinCodeSet);
        cbRemove = findViewById(R.id.cb_pinCodeRemove);
    }

    public void listeners() {
        btnSetCode.setOnClickListener(this);
        cbRemove.setOnClickListener(this);
    }

    public void clearFields () {
        etPinCode.setText("");
        cbRemove.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_pinCodeSet) {

            SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
            String isPinSet = sharedPreferences.getString("pin","hello");
            if (cbRemove.isChecked()) {
                etPinCode.setText("");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pin", "hello");
                editor.commit();
                Toast.makeText(this, "Your pin has successfully removed", Toast.LENGTH_SHORT).show();
            } else {
                if (isPinSet.equals("hello")) {
                    String pinCode = etPinCode.getText().toString();
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
    }
}
