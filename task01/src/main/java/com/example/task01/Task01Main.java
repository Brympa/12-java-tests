package com.example.task01;

public class Task01Main {
    public static void main(String[] args) {
        System.out.println("Integer.decode(\"123\") = " + Integer.decode("123"));
        System.out.println("Integer.decode(\"-123\") = " + Integer.decode("-123"));
        System.out.println("Integer.decode(\"+123\") = " + Integer.decode("+123"));
        System.out.println("Integer.decode(\"0x7F\") = " + Integer.decode("0x7F"));
        System.out.println("Integer.decode(\"0X7F\") = " + Integer.decode("0X7F"));
        System.out.println("Integer.decode(\"#7F\") = " + Integer.decode("#7F"));
        System.out.println("Integer.decode(\"077\") = " + Integer.decode("077"));
        System.out.println("Integer.decode(\"-2147483648\") = " + Integer.decode("-2147483648"));
    }
}
