package com.example.leetcode.Day202109;

public class Solution二分查找 {
        public static int search(int[] nums, int target) {
            int left = 0, right = nums.length - 1;
            while(left<=right) {
                System.out.println("left = "+left+"; right = "+right);
                int mid = left + (right - left) / 2;
                if(nums[mid] == target) {
                    return mid;
                } else if(nums[mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return -1;
        }

        public static void main(String[] args){
            int[] nums = new int[]{0,1,2,3,4,5,6,7,8,9};
            int target = 9;
            int index = search(nums, target);
            System.out.println(index);
        }

}
