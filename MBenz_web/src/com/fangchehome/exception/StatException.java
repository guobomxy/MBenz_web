package com.fangchehome.exception;

public class StatException extends RuntimeException {
    private static int type;

    public static int UNKNOWN_TYPE = 0;

    public static int SYSTEM_TYPE = 1;

    public static int DB_TYPE = 2;

    public static int BIZ_TYPE = 3;

    public StatException(String msg) {
        super(msg);
    }
    
    public StatException(Exception e) {
        super(e);
    }

    public StatException(int t, String msg) {
        super(msg);
        type = t;
    }

    public int getLevel() {
        return type;
    }

}