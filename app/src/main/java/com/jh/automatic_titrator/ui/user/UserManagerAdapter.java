package com.jh.automatic_titrator.ui.user;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.entity.common.Role;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/10/10.
 */
public class UserManagerAdapter extends BaseAdapter {

    private List<User> users;

    private Context context;

    private Set<Integer> checkedPositions;

    private String[] userTypes;

    private Handler handler;

    private int role;

    private UserHelper userHelper;

    public UserManagerAdapter(List<User> users, Context context, Handler handler, UserHelper userHelper) {
        this.users = users;
        this.context = context;
        checkedPositions = new HashSet<>();
        userTypes = context.getResources().getStringArray(R.array.user_type);
        this.handler = handler;
        this.role = Cache.getUser().getRole().ordinal();
        this.userHelper = userHelper;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<User> getCheckedUsers() {
        List<User> checkedUsers = new ArrayList<>();
        for (int position : checkedPositions) {
            checkedUsers.add(users.get(position));
        }
        return checkedUsers;
    }

    @Override
    public void notifyDataSetChanged() {
        checkedPositions.clear();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_manager_list_item, null);

        final User user = users.get(position);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.use_manager_list_item_cb);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedPositions.add(position);
                } else {
                    checkedPositions.remove(position);
                }
            }
        });
        if (!Cache.getUser().getUserName().equals("admin") && user.getRole().equals(Role.manager)) {
            checkBox.setVisibility(View.INVISIBLE);
        }

        TextView sortId = (TextView) view.findViewById(R.id.use_manager_list_item_sortid);
        sortId.setText((position + 1) + "");

        TextView username = (TextView) view.findViewById(R.id.use_manager_list_item_username);
        username.setText(user.getUserName());

        TextView creator = (TextView) view.findViewById(R.id.use_manager_list_item_creator);
        creator.setText(user.getCreator());

        TextView date = (TextView) view.findViewById(R.id.use_manager_list_item_date);
        date.setText(user.getCreateDate());

        TextView desc = (TextView) view.findViewById(R.id.use_manager_list_item_desc);
        desc.setText(user.getDesc());

        TextView userType = (TextView) view.findViewById(R.id.use_manager_list_item_usertype);
        switch (user.getRole()) {
            case manager:
                userType.setText(userTypes[0]);
                break;
            case operator:
                userType.setText(userTypes[1]);
                break;
        }

        Button lockBtn = view.findViewById(R.id.use_manager_list_item_lock);

        if (this.role < user.getRole().ordinal()) {
            lockBtn.setVisibility(View.VISIBLE);
            final boolean locked = user.getAuth().startsWith("null|");

            String lockStr = locked ? context.getString(R.string.unlock) : context.getString(R.string.lock);

            lockBtn.setText(lockStr);
            lockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (locked) {
                        userHelper.unlockUser(user.getUserName());
                        AuditService.addAuditService(Cache.getUser().getUserName(), context.getString(R.string.help), context.getString(R.string.update), context.getString(R.string.unlock), Cache.currentTime(), DBService.getAuditHelper(context));
                    } else {
                        userHelper.lockUser(user.getUserName());
                        AuditService.addAuditService(Cache.getUser().getUserName(), context.getString(R.string.help), context.getString(R.string.update), context.getString(R.string.lock), Cache.currentTime(), DBService.getAuditHelper(context));
                    }
                    Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                }
            });
        } else {
            lockBtn.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
