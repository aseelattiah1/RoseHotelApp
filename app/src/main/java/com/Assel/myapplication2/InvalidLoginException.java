package com.Assel.myapplication2;

public class InvalidLoginException extends Exception{
    public InvalidLoginException() {
        super("Invalid Login ! ");
    }
    private InvalidLoginException(String msg) {

        super(msg);
    }
}
