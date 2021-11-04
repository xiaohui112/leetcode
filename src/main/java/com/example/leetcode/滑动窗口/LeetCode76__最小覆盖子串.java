package com.example.leetcode.滑动窗口;

public class LeetCode76__最小覆盖子串 {
        public static String minWindow(String s, String t) {
            char[] chars = s.toCharArray(), chart = t.toCharArray();
            int n = chars.length, m = chart.length;
            //ASCII表总长128 所有字母的数组,记录每个字母出现的次数，初始都为0。为什么要--,不是++
            int[] hash = new int[128];
            //用负数表示 t中每个字母出现次数
            for (char ch : chart) {
                hash[ch]--;
            }

            String res = "";
            //i=窗口右边界  j =窗口左边界  cnt =
            for (int i = 0, j = 0, cnt = 0; i < n; i++) {
                //s中字母出现次数+1，
                hash[chars[i]]++;
                //s中的A出现次数 没达到 t中A的个数 cnt++
                if (hash[chars[i]] <= 0){
                    cnt++;
                }
                System.out.println("for======"+s.substring(j, i + 1));
                /**
                 * 窗口左边界右移，一直到 当前窗口中 不包含 所有t中的字符
                 * hash[chars[j]] > 0 说明 s子串中 A出现了2次，而t中只有一个，j指针可以右移
                 */
                while (cnt == m && hash[chars[j]] > 0){
                    System.out.println("while++++"+s.substring(j, i + 1));
                    hash[chars[j++]]--;
                }
                if (cnt == m) //包含所有t中字符
                    if (res.equals("") || res.length() > i - j + 1)//最小子串
                        res = s.substring(j, i + 1);
            }
            return res;
        }
    public static String minWindow2(String s, String t) {
        char[] charS = s.toCharArray(),charT = t.toCharArray();
        int n = charS.length,m = charT.length;
        //定义字符数组，记录字符出现次数, char A 根据ACSII直接可以转成 数字 69，用int[128]数组，下标就是字母，值就是出现次数。有点hashmap的意思
        //如果 s 和 t 不全是有字母组成，这里就不能用int[]了。需要使用hashmap了 k=字符 v=次数
        int[] hash = new int[128];
        //遍历charT,每个字母出现一次减一次
        for(char ch : charT){
            hash[ch]--;
        }
        //开始定义窗口 left=左边界 right=右边界 count=t中所有字符出现的次数
        String ret = "";
        for(int left = 0,right = 0,count = 0;right < n ;right++){
            hash[charS[right]]++;
            if(hash[charS[right]]<=0){//当前字母还不满足 t中需要的次数 count++
                count++;
            }
            //当t中所有字母都出现过了 && 左边界的字母出现次数大于0时 左边界可以右移
            //此时右边界 保持不动
            while(count == m && hash[charS[left]] > 0){
                hash[charS[left]]--;
                left++;
            }
            //左边界不能再右移，就可以判断是不是最小子串了
            if(count==m){
                if(ret.equals("") || ret.length() > right - left + 1){
                    ret = s.substring(left,right+1);
                }
            }
        }
        return ret;
    }
    public static void main(String[] args) {
        String s = "ADOBECODEBANC";
        String t = "ABC";

        System.out.println(minWindow2(s,t));
    }
}
