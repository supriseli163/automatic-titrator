package com.jh.automatic_titrator.exception;

/**
 * Created by apple on 2017/2/12.
 */

public class IndexAlreadyExistException extends Exception {
    public IndexAlreadyExistException() {
        super();
    }

    public IndexAlreadyExistException(String message) {
        super(message);
    }
}
