package com.example.leetcode;

public class 最大子数组和 {
    public static int maxSubArray(int[] nums) {
        int curr = 0;int maxSum = nums[0];
        for(int i = 0; i<nums.length; ++i){
            curr = Math.max(curr+nums[i],nums[i]);
            maxSum = Math.max(maxSum,curr);
            System.out.println("nums[i] = "+nums[i]+",curr = "+curr+",maxSum = "+maxSum);
        }
        return maxSum;
    }

    public static void main(String[] args){
        maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4});
    }
}