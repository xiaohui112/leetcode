package com.example.JUC.LRU;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LRUCacheMode extends LinkedHashMap {

    private int capacity;//初始化容量

    public LRUCacheMode(int capacity){
        /**
         *      * @param  initialCapacity the initial capacity
         *      * @param  loadFactor      the load factor
         *      * @param  accessOrder     the ordering mode
         *          - <tt>true</tt> for access-order,    访问顺序排队
         *          - <tt>false</tt> for insertion-order 插入顺序排队
         */
        super(capacity,0.75f,true);
        this.capacity = capacity;
    }

    /**
     * 此方法的定义为；返回true时，LinkedHashMap会删除最老的节点，这里的最老的，根据构造方法中的accessOrder来计算
     * 父类中此方法return false; 所以LinkedHashMap不会删除老节点
     *
     * 这里重写此方法，当容量大于初始化容量时，新插入的数据时，老节点会删除
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return super.size() > capacity;
    }


    public static void main(String[] args) {
        LRUCacheMode mode = new LRUCacheMode(3);
         mode.put(1,1);
         mode.put(2,2);
         mode.put(3,3);

        System.out.println(mode.keySet());
        //新插入4，容量已满时，最先插入的 1会被删除
        mode.put(4,4);
        System.out.println(mode.keySet());

        //访问一个key，根据accessOrder刷新key的新鲜度
        mode.get(3);
        System.out.println(mode.keySet());
        mode.put(3,1000);
        System.out.println(mode.keySet());
        mode.put(3,"100");
        System.out.println(mode.keySet());

        //在插入新key，
        mode.put(5,5);
        System.out.println(mode.keySet());

    }

    /**
     * [1, 2, 3]
     * [2, 3, 4]
     * [2, 4, 3]
     * [2, 4, 3]
     * [2, 4, 3]
     * [4, 3, 5]
     */
}
