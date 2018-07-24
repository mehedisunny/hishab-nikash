package com.example.mehedi.hishabnikash.pin_code;

import android.annotation.SuppressLint;
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

public class SecurityQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstAnswer, secondAnswer, thirdAnswer;
    private Button btnSetQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        // initializing the components
        init();

        // setting up the listeners
        setListener();
    }

    public void init () {
        firstAnswer = findViewById(R.id.et_securityQuestionOne);
        secondAnswer = findViewById(R.id.et_securityQuestionTwo);
        thirdAnswer = findViewById(R.id.et_securityQuestionThree);
        btnSetQuestion = findViewById(R.id.btn_setSecurityQuestion);
    }

    public void setListener () {
        btnSetQuestion.setOnClickListener(this);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_setSecurityQuestion) {
            String questionOne = firstAnswer.getText().toString().trim();
            String questionTwo = secondAnswer.getText().toString().trim();
            String questionThree = thirdAnswer.getText().toString().trim();

            if (questionOne.isEmpty() || questionTwo.isEmpty() || questionThree.isEmpty()) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("firstAnswer", questionOne);
                editor.putString("secondAnswer", questionTwo);
                editor.putString("thirdAnswer", questionThree);
                editor.commit();

                Toast.makeText(this, "You have successfully set your security questions", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }
}
