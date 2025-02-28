package com.hunt.cs360finalproject.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hunt.cs360finalproject.R;
import com.hunt.cs360finalproject.datamanagement.WeightDatabase;
import com.hunt.cs360finalproject.login.behaviors.LoginButtonBehavior;
import com.hunt.cs360finalproject.login.behaviors.signupButtonBehavior;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private Button signupButton;
    private EditText usernameField;
    private EditText passwordField;
    private TextView errorTextField;
    private WeightDatabase weightdb;
    private final String USERNAME_STATE_KEY = "username";
    private final String PASSWORD_STATE_KEY = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();
        reloadPreviousState(savedInstanceState);
        weightdb.getReadableDatabase();
    }

    private void initComponents(){
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.create_account_button);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        errorTextField = findViewById(R.id.login_error_message);
        weightdb = new WeightDatabase(this, null, null, 1);

        addListenersToButtons();
    }

    private void addListenersToButtons(){
        loginButton.setOnClickListener(new LoginButtonBehavior(usernameField, passwordField, weightdb, errorTextField));
        signupButton.setOnClickListener(new signupButtonBehavior(usernameField, passwordField, weightdb, errorTextField));
    }

    private void reloadPreviousState(Bundle state){
        if(state != null){
            usernameField.setText(state.getString(USERNAME_STATE_KEY));
            passwordField.setText(state.getString(PASSWORD_STATE_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(USERNAME_STATE_KEY, usernameField.getText().toString());
        outState.putString(PASSWORD_STATE_KEY, passwordField.getText().toString());
    }
}