package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j(topic = "c")
public class Test39_AtomicIntegerArray {

    public static void main(String[] args){
        demo(
                ()-> new int[10],
                array -> array.length,
                (array,index) -> array[index]++,
                array -> System.out.println(Arrays.toString(array))
        );
        //[8254, 8171, 8120, 8073, 8074, 8058, 8029, 8039, 8214, 8343]
        //非原子数组线程不安全，存在多个线程修改重复的情况

        demo(
                () -> new AtomicIntegerArray(10),
                array -> array.length(),
                (array,index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
        //原子数组 线程安全，每次操作都成功
    }
    
    
    /**
     *
     * @param arraySupplier 参数1，提供数组，可以是线程不安全数组 或 线程安全数组
     * @param lengthFun     参数2，获取数组长度的方法
     * @param putConsumer   参数3，自增方法，会传 array, index
     * @param printConsumer 参数4，打印数组方法
     * @param <T>
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,          //  supplier 提供者  无中生有  ()->结果
            Function<T,Integer> lengthFun,      //  function 函数  一个参数一个结果   (param)->结果。     BiFunction (param1,param2)->结果
            BiConsumer<T,Integer> putConsumer,  //  consumer 消费者  一个参数无结果   (param)->void.      BiConsumer(param1,param2)->void
            Consumer<T> printConsumer){
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            //每个线程对数组10000次操作，分散到各个元素  j%length
            ts.add(new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array,j % length);
                }
            }));
        }
        
        ts.forEach(thread -> thread.start());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        printConsumer.accept(array);
    }
}


