package com.jh.automatic_titrator.service;

import java.util.concurrent.locks.ReadWriteLock;

public class TitratorTestService {
    private static TitratorTestService instance = new TitratorTestService();

    public static TitratorTestService getInstance() {
        return instance;
    }


}
