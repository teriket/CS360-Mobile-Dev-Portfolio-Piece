package com.hunt.cs360finalproject.login.behaviors;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hunt.cs360finalproject.datamanagement.WeightDatabase;

public class LoginButtonBehavior extends LoginScreenClickable implements View.OnClickListener {
    TextView errorMessage;
    public LoginButtonBehavior(EditText usernameField, EditText passwordField, WeightDatabase db, TextView errorField){
        super(usernameField, passwordField, db);
        errorMessage = errorField;
    }

    @Override
    public void onClick(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.isBlank() || password.isBlank()){
            errorMessage.setText("You must input a username and password");
            return;
        }

        long id = db.getUserID(username, password);

        //if no username / password combination is found
        if(id == -1){
            errorMessage.setText("Incorrect username or password");

        }

        //valid username / password combination, login to weights screen
        else{
            login(view.getContext(), id);
        }
    }
}
