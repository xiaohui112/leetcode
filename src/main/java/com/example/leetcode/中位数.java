package com.example.leetcode;

import java.util.Arrays;

public class 中位数 {

        public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
            int[] all = new int[nums1.length + nums2.length];
            for (int i = 0; i < nums1.length; i++) {
                all[i] = nums1[i];
            }
            for (int i = 0; i < nums2.length; i++) {
                all[nums1.length + i] = nums2[i];
            }
            Arrays.sort(all);
            int mid = (all.length-1) / 2;
            if(all.length % 2 == 0){//偶数个
                return (all[mid] + all[mid+1])/2.00000;
            }else{//计数个
                return all[mid];
            }
        }

        public static void main(String[] args){
            double medianSortedArrays = findMedianSortedArrays(new int[]{1, 3}, new int[]{2,4});
            System.out.println(medianSortedArrays);

        }
}
