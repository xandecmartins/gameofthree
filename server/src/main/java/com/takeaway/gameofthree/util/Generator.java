package com.takeaway.gameofthree.util;
public class Generator{
    private static int counter=1;

    public static synchronized int getId(){
        return counter++;
    }
}