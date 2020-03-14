package com.jh.automatic_titrator.service;

public class TitorMethodService {
    public static TitorMethodService getInstance() {
        return ourInstance;
    }

    private static TitorMethodService ourInstance = new TitorMethodService();
}
