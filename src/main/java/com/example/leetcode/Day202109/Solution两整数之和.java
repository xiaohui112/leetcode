package com.example.leetcode.Day202109;

public class Solution两整数之和 {
    /**
     * 给你两个整数 a 和 b ，不使用 运算符 + 和 - ​​​​​​​，计算并返回两整数之和。
     * @param a
     * @param b
     * @return
     */


    public static int getSum(int a, int b) {
        while(b!=0){
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }


    public static void main(String[] args){
        int a = -5;
        int b = 6;
        System.out.println((a & b) << 1);
        System.out.println(a);
        System.out.println(b);
        System.out.println(getSum(a,b));
    }
}
