package com.example.leetcode.滑动窗口;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LeetCode3无重复字符的最长子串 {
    /**
     * 双指针 i 和 j同时指向第一个字符，j向右移，i不动。直到子串中出现重复字符
     *        i
     *    d-v-d-f
     *    j
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        if(s.length()==0){
            return 0;
        }
        int left = 0,right = 0;
        int max = 0;
        Map<Character,Integer> map = new HashMap();
        while (right<s.length()) {
            if(map.containsKey(s.charAt(right))){
                //这里要用Math.max比较一下，防止left指针回跳
                left = Math.max(left,map.get(s.charAt(right))+1);
            }
            map.put(s.charAt(right),right);
            max = Math.max(max,right-left+1);
            //max赋值必须放在这。防止没有重复字符，max一直是0，虽然每次循环都赋值，有点浪费
            right++;
        }
        return max;
    }

    public static void main(String[] args) {
        int maxlength = lengthOfLongestSubstring("abba");
        System.out.println(maxlength);
    }
}
