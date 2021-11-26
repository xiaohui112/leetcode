package com.example.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class 只出现一次的数 {
    public static void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        Map<Integer,Integer> map = new HashMap();
        for(int i : array){
            if(map.containsKey(i)){
                map.remove(i);
            }else{
                map.put(i,i);
            }
        }
        int count = 0;
        Set<Integer> keys = map.keySet();
        for (Integer key: keys) {
            if(count==0){
                num1[0] = key.intValue();
                count++;
            }else{
                num2[0] = key.intValue();
            }
        }

    }

    public static void main(String[] args){
        int[] num1 =new int[1];
        int[] num2 =new int[1];
        FindNumsAppearOnce(new int[]{1,2,3,4,1,2},num1,num2);
        System.out.println(num1[0]);
        System.out.println(num2[0]);
    }
}