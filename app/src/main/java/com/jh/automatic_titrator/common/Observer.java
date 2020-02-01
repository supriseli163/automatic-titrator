package com.jh.automatic_titrator.common;

/**
 * Created by apple on 2016/10/19.
 */
public interface Observer {

    int ADD = 1;

    int REFRESH_BASE = 0;

    int CLEAR = -1;

    public void notifyDataChanged(int sig);

}
