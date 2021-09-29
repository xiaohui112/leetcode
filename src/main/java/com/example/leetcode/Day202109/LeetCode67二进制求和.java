package com.example.leetcode.Day202109;

public class LeetCode67二进制求和 {
    /***
     * 给你两个二进制字符串，返回它们的和（用二进制表示）。
     * 输入为 非空 字符串且只包含数字 1 和 0。
     * 示例 1:
     * 输入: a = "11", b = "1"
     * 输出: "100"
     * 示例 2:
     * 输入: a = "1010", b = "1011"
     * 输出: "10101"
     *
     */


    public static String addBinary(String a, String b) {
        StringBuffer strb = new StringBuffer();
        int i = 0,j = 0;
        int carry = 0;
        while(i<a.length() || j < b.length()) {
            carry += i < a.length() ? a.charAt(a.length() - i - 1) - '0' : 0;
            carry += j < b.length() ? b.charAt(b.length() - j - 1) - '0' : 0;
            strb.append(carry % 2);//求余数，放入字符串

            carry = carry / 2;//保留进位，下一次循环使用
            i++;
            j++;
        }
        //遍历完成后，还有进位，别忘了加上
        if(carry>0){
            strb.append(carry);
        }
        //翻转字符串
        strb.reverse();
        return strb.toString();
    }



    public static void main(String[] args){
        System.out.println(addBinary("11","1"));

    }
}
