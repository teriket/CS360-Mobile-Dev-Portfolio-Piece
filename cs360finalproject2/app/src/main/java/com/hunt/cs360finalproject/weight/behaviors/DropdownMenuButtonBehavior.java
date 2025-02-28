package com.hunt.cs360finalproject.weight.behaviors;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import com.hunt.cs360finalproject.R;
import com.hunt.cs360finalproject.login.MainActivity;

public class DropdownMenuButtonBehavior implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        //generate menu
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.dropdown_menu, menu.getMenu());

        //Give behaviors to every button in menu.  Only one item currently, but left as switch
        //statement if more are added later.
        menu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getOrder()){
                //logout button
                case 0:
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    break;

                    //not found
                default:
                    Log.d("d", "could not find menu item");
                    break;
            }
            return true;
        });

        menu.show();
    }
}
