package com.hunt.cs360finalproject.login.behaviors;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hunt.cs360finalproject.datamanagement.WeightDatabase;

public class signupButtonBehavior extends LoginScreenClickable implements View.OnClickListener {
    TextView errorMessageField;
    public signupButtonBehavior(EditText usernameField, EditText passwordField, WeightDatabase db, TextView errorMessageField){
        super(usernameField, passwordField, db);
        this.errorMessageField = errorMessageField;
    }

    @Override
    public void onClick(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        //user trying to create an account with blank space
        if(username.isBlank() || password.isBlank()){
            errorMessageField.setText("You must enter a username and password");
            return;
        }

        //user trying to create a username with an existent account
        if(db.usernameExists(username)){
            errorMessageField.setText("An account with that username already exists");
            return;
        }

        //successful account creation, automatically log in with the new account
        else{
            long id = db.addUser(username, password);
            login(view.getContext(), id);
        }

    }
}
