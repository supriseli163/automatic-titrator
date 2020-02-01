package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.Role;
import com.jh.automatic_titrator.entity.common.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/10/16.
 */
public class UserHelper {

    public static final String SUPER_ADMIN = "400808";

    public static final String SUPER_PASSWORD = SUPER_ADMIN;

    public static final String INIT_PASSWORD = "888888";

    private static final int HISTORY_SIZE = 20;

    private SQLiteDatabase db;

    public UserHelper(DBHelper dbHelper) {
        db = dbHelper.getWritableDatabase();
//        dbHelper.addBaseUser(db);
//        dbHelper.onUpgrade(db, 1, 2);
    }

    public void close() {
        db.close();
    }

    public int login(String username, String password) {
        if (username.equals("admin") && password.equals(SUPER_ADMIN)) {
            resetAdminPswd();
            return 1;
        }
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            Cursor cursor = db.rawQuery("select * from user where username = \"" + username + "\"", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                String password1 = cursor.getString(1);
                String auth = cursor.getString(6);
                if (password.equals(password1)) {
                    if (!auth.startsWith("null|")) {
                        updateUserHistory(username);
                        return 1;
                    } else {
                        return 2;
                    }
                }
            }
            cursor.close();
        } else {
            return 0;
        }
        return -1;
        //return 1;
    }

    private void updateUserHistory(String username) {
        Cursor cursor = db.rawQuery("select count(*) from history_user", null);
        cursor.moveToFirst();
        int count = 0;
        if (!cursor.isAfterLast()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.execSQL("delete from history_user where userName='" + username + "'");
        db.execSQL("insert into history_user (userName) values('" + username + "')");
        if (HISTORY_SIZE < count) {
            db.execSQL("delete from history_user where id=(select min(id) from `history_user`)");
        }
    }

    public String getUserAuth(String username) {
        Cursor cursor = db.rawQuery("select * from user where username = '" + username + "'", null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getString(6);
        } else {
            return null;
        }
    }

    public User getUserDetail(String username) {
        if (username.equals(SUPER_ADMIN)) {
            username = "admin";
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from user where username = '" + username + "'", null);
            cursor.moveToFirst();
            User user = new User();
            if (!cursor.isAfterLast()) {
                user.setUserName(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setRole(Role.getRole(cursor.getInt(2)));
                user.setCreateDate(cursor.getString(3));
                user.setDesc(cursor.getString(4));
                user.setHeadId(cursor.getInt(5));
                user.setAuth(cursor.getString(6));
                user.setCreator(cursor.getString(7));
                user.setSettingConfig(cursor.getString(8));
                return user;
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int addUser(User user) {
        User oldUser = getUserDetail(user.getUserName());
        if (oldUser != null) {
            return -1;
        } else {
            String insertSql = "insert into user (userName, " +
                    "password, role, createdate, desc, headid, auth, creator)" +
                    " values (" +
                    "'" + user.getUserName() + "'," +
                    "'" + user.getPassword() + "'," +
                    "'" + Role.getRoleIndex(user.getRole()) + "'," +
                    "'" + user.getCreateDate() + "'," +
                    "'" + user.getDesc() + "'," +
                    "" + user.getHeadId() + "," +
                    "'" + user.getAuth() + "'," +
                    "'" + user.getCreator() + "'" +
                    ");";
            db.execSQL(insertSql);
            return 0;
        }
    }

    public void deleteUser(User user) {
        deleteUser(user.getUserName());
    }

    public void deleteUser(String username) {
        String deleteSql = "delete from user where username = '" + username + "'";
        db.execSQL(deleteSql);
    }

    public void deleteUsers(List<User> users) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from user where username in (");
        for (User user : users) {
            deleteSql.append("'" + user.getUserName() + "',");
        }
        deleteSql.deleteCharAt(deleteSql.length() - 1);
        deleteSql.append(")");
        db.execSQL(deleteSql.toString());
    }

    public int updatePassword(String username, String oldpassword, String newPassword) {
        int loginStatus = login(username, oldpassword);
        if (loginStatus == 0) {
            String updatePassword = "update user set password = '" + newPassword + "' where username = '" + username + "'";
            db.execSQL(updatePassword);
            return 0;
        } else {
            return -2;
        }
    }

    public void updateUser(User user) {
        StringBuilder updateSB = new StringBuilder();
        updateSB.append("update user set ");
        updateSB.append("password = '").append(user.getPassword()).append("', ");
        updateSB.append("role = ").append(Role.getRoleIndex(user.getRole())).append(", ");
        updateSB.append("createdate = '").append(user.getCreateDate()).append("', ");
        updateSB.append("desc = '").append(user.getDesc()).append("', ");
        updateSB.append("headid = '").append(user.getHeadId()).append("', ");
        updateSB.append("auth = '").append(user.getAuth()).append("', ");
        if (user.getSettingConfig() != null) {
            updateSB.append("conf = '").append(user.getSettingConfig()).append("', ");
        }
        updateSB.append("creator = '").append(user.getCreator()).append("' ");

        updateSB.append("where userName = '").append(user.getUserName()).append("'");
        db.execSQL(updateSB.toString());
    }

    public List<User> listUser(String currentUser) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from user where role <> 0", null);
            cursor.moveToFirst();
            List<User> users = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                User user = new User();
                String username = cursor.getString(0);
                if (!username.equals(currentUser)) {
                    user.setUserName(username);
                    user.setPassword(cursor.getString(1));
                    user.setRole(Role.getRole(cursor.getInt(2)));
                    user.setCreateDate(cursor.getString(3));
                    user.setDesc(cursor.getString(4));
                    user.setHeadId(cursor.getInt(5));
                    user.setAuth(cursor.getString(6));
                    user.setCreator(cursor.getString(7));
                    byte[] conf = cursor.getBlob(8);
                    if (conf != null && conf.length > 0) {
                        user.setSettingConfig(new String(conf));
                    }
                    users.add(user);
                }
                cursor.moveToNext();
            }
            return users;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void resetAdminPswd() {
        String updatePassword = "update user set password = '" + INIT_PASSWORD + "' where username = 'admin'";
        db.execSQL(updatePassword);
    }

    public void changeUsername(String username, String newUsername) {
        String update = "update user set username = '" + newUsername + "' where username = '" + username + "'";
        db.execSQL(update);
        db.execSQL("update history_user set username = '" + newUsername + "' where username = '" + username + "'");
    }

    public void changePassword(String username, String newPassword) {
        String updatePassword = "update user set password = '" + newPassword + "' where username = '" + username + "'";
        db.execSQL(updatePassword);
    }

    public List<String> historyUser() {
        List<String> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from history_user order by id desc", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                users.add(cursor.getString(1));
                cursor.moveToNext();
            }
            return users;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getAutoLoginUser() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from last_user ", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public void setAutoLoginUser(String userName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from last_user ", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int count = cursor.getInt(0);
                if (count == 0) {
                    db.execSQL("insert into last_user(userName) values('" + userName + "')");
                } else {
                    db.execSQL("update last_user set userName = '" + userName + "'");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void cleanAutoLoginUser() {
        db.execSQL("delete from last_user");
    }

    public void lockUser(String username) {
        String auth = getUserAuth(username);
        if (!auth.startsWith("null|")) {
            String update = "update user set auth = 'null|" + auth + "' where username = '" + username + "'";
            db.execSQL(update);
        }
    }

    public void unlockUser(String username) {
        String auth = getUserAuth(username);
        if (auth.startsWith("null|")) {
            auth = auth.substring(5);
            String update = "update user set auth = '" + auth + "' where username = '" + username + "'";
            db.execSQL(update);
        }
    }
}
