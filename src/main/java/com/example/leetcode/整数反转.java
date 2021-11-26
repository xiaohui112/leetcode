package com.example.leetcode;

public class 整数反转 {
    public static long reverse(int x) {
        long ans = 0;
        while(x!=0){
            int i = x % 10;
            System.out.println("x = "+x+",i = "+i+",ans = "+ans);
            ans = ans*10 + i;
            x = x / 10;
        }
//        return (int)ans == ans?(int)ans:0;
        return ans;
    }
    public static void main(String[] args){
        System.out.println(reverse(Integer.MAX_VALUE));
    }
}