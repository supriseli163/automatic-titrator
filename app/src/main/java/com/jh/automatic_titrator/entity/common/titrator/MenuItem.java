package com.jh.automatic_titrator.entity.common.titrator;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuItem implements Serializable {

    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}