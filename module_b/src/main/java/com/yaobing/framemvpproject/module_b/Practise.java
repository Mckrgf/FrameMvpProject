package com.yaobing.framemvpproject.module_b;

public class Practise {
    public static void main(String[] args) {
        int n1 = 1;
        int n2 = 2;
        int n3 = 3;
        int n4 = 14;
        int n5 = 17;
        int a = getBQ(n1);
    }

    private static int getBQ(int n) {
        if (n==1 || n==2) {
            return 1;
        }
        return 0;
    }
}
