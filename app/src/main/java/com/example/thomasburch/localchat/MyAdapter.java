package com.example.thomasburch.localchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import android.graphics.Color;

public class MyAdapter extends ArrayAdapter<ListElement> {

    int resource;
    Context context;
    String my_user_id;
    public static String LOG_TAG = "ChatApplication";

    public MyAdapter(Context _context, int _resource, List<ListElement> items, String _user_id) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
        my_user_id = _user_id;
        //Log.i(LOG_TAG, "my_user_id: " + my_user_id);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        ListElement w = getItem(position);

        newView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
        //LayoutInflater vi =  getContext().getSystemService(inflater);

        if(convertView == null) {

            if (my_user_id.equals(w.user_id)) {
                vi.inflate(R.layout.r_list_element, newView, true);
            } else {
                vi.inflate(R.layout.list_element, newView, true);
            }
        }else {
            if (my_user_id.equals(w.user_id)) {
                vi.inflate(R.layout.r_list_element, newView, true);
                //tv.setTextColor(Color.parseColor("#00FF00")); //this is green color
            } else {
                vi.inflate(R.layout.list_element, newView, true);
                //tv.setTextColor(Color.parseColor("#000FFF"));
            }
            newView = (LinearLayout) convertView;
            //newView = convertView;
        }

        TextView tv = (TextView) newView.findViewById(R.id.get_message);
        if(my_user_id.equals(w.user_id)) {
            //convertView = vi.inflate(R.layout.r_list_element, newView, true);
            tv.setTextColor(Color.parseColor("#00FF00")); //this is green color
        } else {
            //convertView = vi.inflate(R.layout.list_element, newView, true);
            tv.setTextColor(Color.parseColor("#000FFF")); //this is blue color
        }

        if(w.timestamp.equals("now")){
            tv.setTextColor(Color.parseColor("#FF0000")); //this is red color
        }

        String text = (w.message + "\n\n" + w.nickname);
        tv.setText(text);

        return newView;
    }
}