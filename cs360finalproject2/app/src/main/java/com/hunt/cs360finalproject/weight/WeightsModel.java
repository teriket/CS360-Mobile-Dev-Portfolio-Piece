package com.hunt.cs360finalproject.weight;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hunt.cs360finalproject.datamanagement.WeightDatabase;

/**
 * Contains the business logic of the user_weights screen*/
public class WeightsModel {
    private WeightValues[] values;
    private static WeightsModel instance;
    private int weightGoal;
    private boolean hasReachedHalfWay = false;
    private boolean hasReachedGoal = false;
    WeightDatabase db;
    Context context;

    private WeightsModel(){}

    public static WeightsModel getInstance(){
        if(instance==null){
            instance = new WeightsModel();
        }
        return instance;
    }

    /**
     * load in the user weights values, and decide what information
     * to attach to the weight i.e. congratulations and weight change
     * direction.*/
    public WeightValues[] getUserWeights(Context context, int userID){
        db = new WeightDatabase(context);
        values = new WeightValues[db.getNumUserWeights(userID)];
        this.context = context;

        setWeightGoal(userID);

        for(int i = 0; i < values.length; i++){
            WeightValues value = new WeightValues(i);

            //set the date
            String date = db.getWeightDate(userID, i);
            value.setDate(date);

            //Set the weight
            int weight = db.getWeight(userID, i);
            value.setWeight(weight);

            //Set weight change direction image
            value.setDirection(chooseDirectionImage(weight, i));

            setCongratulation(value);

            values[i] = value;
        }

        //I've dug my own grave and implemented the list in the wrong order
        //I don't have time to redo my code, and I've paid the price with
        //a costly array reversal :(
        for(int i = 0; i < (int)(values.length / 2); i++){
            WeightValues temp = values[i];
            values[i] = values[values.length - 1 - i];
            values[values.length - 1 - i] = temp;
        }

        return values;
    }

    private int chooseDirectionImage(int curWeight, int index){
        int dir = 0;
        if(index == 0){
            return dir;
        }
        int difference = curWeight - values[index - 1].getWeight();

        //See if the weight went up, down, or stayed the same
        if(difference < 0){
            dir = -1;
        } else if (difference == 0) {
            dir = 0;
        }
        else dir = 1;

        return dir;
    }

    private void setWeightGoal(int userID){
        this.weightGoal = db.getLatestGoalWeight(userID);
    }

    private void setCongratulation(WeightValues value){
        if((value.getWeight() <= weightGoal)
            && !hasReachedGoal){
            value.setToastable("You've achieved your goal!");
            hasReachedGoal = true;
            sendSMS();
        }
        else if((value.getWeight() <= (int)(weightGoal / 2)
                && !hasReachedHalfWay
                && !hasReachedGoal)){
            value.setToastable("You've made it half way");
            hasReachedHalfWay = true;
        }
        else value.setUntoastable();

    }

    private void sendSMS(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED){
            SmsManager messenger = SmsManager.getDefault();
            try{
                SubscriptionManager phoneService = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                String number = phoneService.getPhoneNumber(SubscriptionManager.DEFAULT_SUBSCRIPTION_ID);
                String message = "Congratulations on reaching  " + weightGoal + " lbs!";
                messenger.sendTextMessage(number, null, message, null, null);

            } catch (Exception e) {
                return;
            }

        }
        else{
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.SEND_SMS}, 101);
        }
    }

}
