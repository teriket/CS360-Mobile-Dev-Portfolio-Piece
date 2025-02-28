package com.hunt.cs360finalproject.weight.behaviors;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hunt.cs360finalproject.datamanagement.WeightDatabase;
import com.hunt.cs360finalproject.weight.user_weights;


public class SubmitNewWeightButtonBehavior implements View.OnClickListener {
    EditText inputField;
    long userID;
    boolean isGoalWeight = false;
    Dialog parentDialog;

    public SubmitNewWeightButtonBehavior(EditText inputField, long userID, Dialog dialog){
        this.inputField = inputField;
        this.userID = userID;
        this.parentDialog = dialog;
    }

    public SubmitNewWeightButtonBehavior setGoalWeight(){
        this.isGoalWeight = true;
        return this;
    }

    @Override
    public void onClick(View view) {
        //reject empty inputs and too long inputs, then notify user
        if(inputField.getText().isEmpty() || inputField.getText().length() >= 4){
            Toast.makeText(view.getContext(), "please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        //valid input, submit to database
        long newWeight = Long.parseLong(inputField.getText().toString());
        WeightDatabase db = new WeightDatabase(view.getContext());
        db.addWeight(userID, newWeight, isGoalWeight);
        //Refresh the data -- there's got to be a better way to update the listview, but it's being
        //stubborn and I'm out of time :(
        Intent intent = new Intent(view.getContext(), user_weights.class);
        intent.putExtra("user_id", userID);
        view.getContext().startActivity(intent);
        
        //toast the user to let them know the operation was successful
        String toastMessage = "added new weight";
        if(isGoalWeight){
            toastMessage = "set new goal";
        }
        Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        
        //close the dialog
        parentDialog.dismiss();

    }
}
