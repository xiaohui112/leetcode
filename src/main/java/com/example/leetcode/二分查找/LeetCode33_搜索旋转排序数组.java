package com.example.leetcode.二分查找;

public class LeetCode33_搜索旋转排序数组 {
    public static int search(int[] nums, int target) {
        int left=0,right=nums.length -1;
        while(left<=right)
        {
            int mid=(left+right)/2;
            if(nums[mid]==target)
                return mid;
            else if(nums[left]<=nums[mid])
            {
                if(target<nums[mid]&&target>=nums[left])
                    right=mid-1;
                else
                    left=mid+1;
            }
            else
            {
                if(target<=nums[right]&&target>nums[mid])
                    left=mid+1;
                else
                    right=mid-1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {5, 1, 3};
        int target = 0;
        int result = search(nums, target);
        System.out.println(result);
    }
}
