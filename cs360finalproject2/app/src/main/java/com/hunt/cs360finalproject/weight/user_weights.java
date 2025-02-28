package com.hunt.cs360finalproject.weight;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hunt.cs360finalproject.R;
import com.hunt.cs360finalproject.datamanagement.WeightDatabase;
import com.hunt.cs360finalproject.weight.behaviors.DropdownMenuButtonBehavior;
import com.hunt.cs360finalproject.weight.behaviors.FloatingActionButtonBehavior;
import com.hunt.cs360finalproject.weight.behaviors.SetGoalWeightButtonBehavior;

/**
 * Updates the user interface of the user_weights screen*/
public class user_weights extends AppCompatActivity {
    long userID;
    ListView weightsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_weights);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //load in the userID for whoever logged in
        Intent intent = getIntent();
        userID = intent.getLongExtra("user_id", 0);

        initComponents();
    }

    /**
     * Bind every component and set its onClickListener*/
    private void initComponents(){
        //Generate the behaviors for the floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FloatingActionButtonBehavior(userID, this));

        //Generate the behaviors for the set new goal weight button
        ImageButton setNewGoalWeightButton = findViewById(R.id.set_goal_weight_button);
        setNewGoalWeightButton.setOnClickListener(new SetGoalWeightButtonBehavior(userID, this));

        //Set the Menu button behavior
        ImageButton openMenu = findViewById(R.id.dropdown_menu_button);
        openMenu.setOnClickListener(new DropdownMenuButtonBehavior());

        //Get the ListView for user weights
        weightsList = findViewById(R.id.weights_container);
        drawWeights();
    }

    public void drawWeights(){
        WeightDatabase db = new WeightDatabase(this);
        int lastWeightIndex = db.getNumUserWeights((int)userID) - 1;

        TextView lastWeightDate = findViewById(R.id.date_card_text_view);

        TextView lastWeight = findViewById(R.id.weight_card_text_view);


        TextView weightGoal = findViewById(R.id.goal_weight_card_text_view);

        //Settng the headers

        //if no weights or goals have been entered
        if(lastWeightIndex < 0){
            lastWeightDate.setText("last weigh in date");
            lastWeight.setText("current weight");
            weightGoal.setText("goal weight");
        }
        else{
            lastWeightDate.setText(db.getWeightDate((int) userID, lastWeightIndex));

            String lastWeightText = "weight: " + db.getWeight((int) userID, lastWeightIndex);
            lastWeight.setText(lastWeightText);

            String weightGoalText = "goal: " + db.getLatestGoalWeight((int)userID);
            weightGoal.setText(weightGoalText);
        }

        weightsList.setAdapter(new weightRowAdapter(this,
                WeightsModel.getInstance().getUserWeights(this, (int) userID)));
    }

}