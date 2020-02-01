package com.jh.automatic_titrator.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jh.automatic_titrator.R;

import java.util.List;

/**
 * Created by apple on 2017/2/6.
 */

public class HistoryUserAdapter extends BaseAdapter {

    private Context context;

    private List<String> historyUsers;

    public HistoryUserAdapter(Context context, List<String> historyUsers) {
        this.context = context;
        this.historyUsers = historyUsers;
    }

    @Override
    public int getCount() {
        return historyUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return historyUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, null);
        String user = historyUsers.get(position);

        TextView userText = (TextView) view.findViewById(R.id.user_item_user);
        if (user != null) {
            userText.setText(user);
        }

//        View line = view.findViewById(R.id.user_item_line);
//        if (position < historyUsers.size() - 1) {
//            line.setVisibility(View.INVISIBLE);
//        } else {
//            line.setVisibility(View.VISIBLE);
//        }

        return view;
    }
}
