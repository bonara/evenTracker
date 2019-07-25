package com.example.eventracker.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventracker.R;
import com.example.eventracker.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> userList;

    public UsersAdapter(Context context, ArrayList<User> list) {
        super(context, 0 , list);
        mContext = context;
        userList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.attendee_row, parent, false);

        User user = userList.get(position);

        ImageView image = listItem.findViewById(R.id.row_avatar);
        image.setImageResource(R.drawable.icon);

        TextView name = listItem.findViewById(R.id.row_username);
        if (user != null) {
            // Populate the data into the template view using the data object
            name.setText(user.getUsername());
        }

        return listItem;
    }

}

