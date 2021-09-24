package com.example.leetcode.Day202109;

import java.util.PriorityQueue;

public class Solution最小K个数 {
    public static void main(String[] args) {

        int k = 3;
        int[] arr = new int[]{5,7,3,0,2,5};
        // 小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        for (int i = 0; i < arr.length; i++) {
            if(heap.size()>=k){
                heap.poll();
                heap.add(arr[i]);
            }else{
                heap.add(arr[i]);
            }
        }

        while (!heap.isEmpty()) { // 排序输出
            System.out.println(heap.poll());
        }
    }

}
