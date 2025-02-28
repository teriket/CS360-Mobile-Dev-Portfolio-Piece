package com.hunt.cs360finalproject.login.behaviors;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.hunt.cs360finalproject.datamanagement.WeightDatabase;
import com.hunt.cs360finalproject.weight.user_weights;

public abstract class LoginScreenClickable {
    protected EditText usernameField;
    protected EditText passwordField;
    protected WeightDatabase db;
    protected final String USER_ID_KEY = "user_id";

    public LoginScreenClickable(EditText usernameField, EditText passwordField, WeightDatabase db){
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.db = db;
    }

    protected void login(Context context, long id){
        Intent intent = new Intent(context, user_weights.class);
        intent.putExtra(USER_ID_KEY, id);
        context.startActivity(intent);
    }
}
