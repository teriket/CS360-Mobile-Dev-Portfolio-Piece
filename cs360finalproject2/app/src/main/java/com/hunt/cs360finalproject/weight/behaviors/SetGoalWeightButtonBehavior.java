package com.hunt.cs360finalproject.weight.behaviors;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hunt.cs360finalproject.R;
import com.hunt.cs360finalproject.weight.user_weights;

public class SetGoalWeightButtonBehavior implements View.OnClickListener {

    long userID;
    user_weights parent;
    public SetGoalWeightButtonBehavior(long userID, user_weights parent){
        this.userID = userID;
    }

    @Override
    public void onClick(View view) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.new_weight_or_goal_dialog);

        TextView userInstructions = dialog.findViewById(R.id.new_weight_or_goal_header);
        userInstructions.setText("Set your goal weight");

        EditText inputField = dialog.findViewById(R.id.set_new_weight_edit_text);

        Button submitButton = dialog.findViewById(R.id.submit_new_weight);
        submitButton.setOnClickListener(new SubmitNewWeightButtonBehavior(inputField, userID, dialog)
                .setGoalWeight());

        dialog.show();
    }
}
