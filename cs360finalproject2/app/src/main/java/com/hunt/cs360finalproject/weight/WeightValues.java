package com.hunt.cs360finalproject.weight;

public class WeightValues {
    private int direction = 0;
    private int weight = 0;
    private String date = "1/1/1";
    private boolean isToastable = true;
    private String toastText = "you did it!";
    private final long weightID;
    public WeightValues(long id){
        weightID = id;
    }

    public void setDirection(int dir){
        direction = dir;
    }

    public int getDirection(){
        return direction;
    }

    public void setWeight(int w){
        weight = w;
    }

    public int getWeight(){
        return weight;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setUntoastable(){
        isToastable = false;
        toastText = "";
    }

    public void setToastable(String toast){
        isToastable = true;
        toastText = toast;
    }

    public boolean getIsToastable(){
        return isToastable;
    }

    public String getToastText(){
        return toastText;
    }

    public long getID(){
        return weightID;
    }
}
