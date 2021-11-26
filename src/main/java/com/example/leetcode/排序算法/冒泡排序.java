package com.example.leetcode.排序算法;

import java.util.Arrays;

public class 冒泡排序 {
    public static void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    public static void bubbleSort_I(int[] arr){
        for (int i = 0; i < arr.length - 1; i++) {//length -1 最后一位不用排了
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if(arr[j] > arr[j+1]){
                    //左边的大数 向右移动
                    swap(arr,j,j+1);
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }
    public static void bubbleSort_II(int[] arr){
        boolean swapped = true;//初始化是swapped为true，否则排序过程无法启动
        for (int i = 0; i < arr.length -1; i++) {
            //如果没有发生过交换，说明剩余的部分已经有序，排序完成
            if(!swapped) break;
            // 设置 swapped 为 false，如果发生交换，则将其置为 true
            swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j+1]){
                    // 如果左边的数大于右边的数，则交换，保证右边的数字最大
                    swap(arr,j,j+1);
                    // 发生过交换
                    swapped = true;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }
    public static void bubbleSort_III(int[] arr){
        boolean swapped = true;
        //最后一个没有经过排序的元素下标
        int indexOfLastUnsortedElement = arr.length-1;
        //上次发生过交换的位置
        int swappedIndex = -1;
        while(swapped){
            swapped = false;
            for (int i = 0; i < indexOfLastUnsortedElement; i++) {
                if (arr[i] > arr[i+1]){
                    swap(arr,i,i+1);
                    swapped = true;
                    swappedIndex = i;
                }
            }
            indexOfLastUnsortedElement = swappedIndex;
        }
        System.out.println(Arrays.toString(arr));
    }

    public static void main(String[] args){
        int[] arr = new int[]{2,3,1,6,4,7,8,5,9};
        bubbleSort_III(arr);
    }
}
