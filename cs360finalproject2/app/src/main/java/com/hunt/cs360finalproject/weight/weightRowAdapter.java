package com.hunt.cs360finalproject.weight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunt.cs360finalproject.R;

public class weightRowAdapter extends BaseAdapter {
    LayoutInflater inflater;
    WeightValues[] data;
    Context context;

    public weightRowAdapter(Context context, WeightValues[] data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[data.length];
    }

    @Override
    public long getItemId(int i) {
        return data[i].getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //instantiate row item
        View rowItem = view;
        if(rowItem == null){
            rowItem = inflater.inflate(R.layout.recycler_view_weight_item, viewGroup, false);
        }

        //get references to each row item component
        ImageView weightDirection = rowItem.findViewById(R.id.weight_direction_image);
        TextView weight = rowItem.findViewById(R.id.weight_value);
        TextView date = rowItem.findViewById(R.id.weight_date);
        ImageView toastImage = rowItem.findViewById(R.id.congratulations_image);
        TextView toastText = rowItem.findViewById(R.id.motivational_message);

        //Populate the data into the rowItem
        setWeightDirectionImage(weightDirection, data[i]);
        //convert the numerical weight into a string before setting the text
        String weightVal = "" + data[i].getWeight();
        weight.setText(weightVal);
        date.setText(data[i].getDate());
        if(data[i].getIsToastable()){
            toastImage.setImageResource(R.drawable.confetti_svgrepo_com);
            toastText.setText(data[i].getToastText());
        }
        else{
            toastImage.setVisibility(View.INVISIBLE);
            data[i].setUntoastable();
            toastText.setText(data[i].getToastText());
        }

        return rowItem;
    }

    public void setWeightDirectionImage(ImageView image, WeightValues values){
        switch(values.getDirection()){
            case -1:
                image.setImageResource(R.drawable.chevron_down_svgrepo_com);
                break;
            case 0:
                image.setImageResource(R.drawable.horizontal_rule_svgrepo_com);
                break;
            case 1:
                image.setImageResource(R.drawable.chevron_up_svgrepo_com);
                break;
        }

    }
}
