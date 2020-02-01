package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 2016/12/19.
 */

public class Audit {
    private int id;

    private String operator;

    private String date;

    private String subFragment;

    private String fragment;

    private String event;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubFragment() {
        return subFragment;
    }

    public void setSubFragment(String subFragment) {
        this.subFragment = subFragment;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
