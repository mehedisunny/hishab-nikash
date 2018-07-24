package com.example.mehedi.hishabnikash.pin_code;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.mehedi.hishabnikash.AboutActivity;
import com.example.mehedi.hishabnikash.CreditsActivity;
import com.example.mehedi.hishabnikash.MainActivity;
import com.example.mehedi.hishabnikash.R;

public class VerifySecurityQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstAnswer, secondAnswer, thirdAnswer;
    private Button btnSetQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_security_question);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializing the components
        init();

        // setting up the listeners
        setListener();
    }

    /*
     * this method is responsible for initializing all the components that have interaction with the
     * activity
     * @param void
     * @return void
     * */
    public void init () {
        firstAnswer = findViewById(R.id.et_securityAnswerOne);
        secondAnswer = findViewById(R.id.et_securityAnswerTwo);
        thirdAnswer = findViewById(R.id.et_securityAnswerThree);
        btnSetQuestion = findViewById(R.id.btn_setSecurityQuestion);
    }


    /*
     * this method is responsible for setting up the listeners
     * @param void
     * @return void
     * */
    public void setListener () {
        btnSetQuestion.setOnClickListener(this);
    }


    /*
     * this method is responsible for clearing all the EditText fields of the form
     * @param void
     * @return void
     * */
    public void clearFields () {
        firstAnswer.setText("");
        secondAnswer.setText("");
        thirdAnswer.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_setSecurityQuestion) {
            String questionOne = firstAnswer.getText().toString().trim();
            String questionTwo = secondAnswer.getText().toString().trim();
            String questionThree = thirdAnswer.getText().toString().trim();

            if (questionOne.isEmpty() || questionTwo.isEmpty() || questionThree.isEmpty()) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            } else {
                int counter = 0;
                SharedPreferences sharedPreferences = getSharedPreferences("HNPIN", MODE_PRIVATE);
                String answerOne = sharedPreferences.getString("firstAnswer", null);
                String answerTwo = sharedPreferences.getString("secondAnswer", null);
                String answerThree = sharedPreferences.getString("thirdAnswer", null);

                if (questionOne.equals(answerOne)) {
                    counter++;
                }

                if (questionTwo.equals(answerTwo)) {
                    counter++;
                }

                if (questionThree.equals(answerThree)) {
                    counter++;
                }

                if (counter >= 2) {
                    SharedPreferences sh = getSharedPreferences("HNPIN", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("pin", "hello");
                    editor.commit();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    clearFields();
                    Toast.makeText(this, "Your answer do not match with the records", Toast.LENGTH_SHORT).show();
                }
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
        } else if (item.getItemId() == R.id.set_security_question) {
            startActivity(new Intent(this, SecurityQuestionActivity.class));
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
