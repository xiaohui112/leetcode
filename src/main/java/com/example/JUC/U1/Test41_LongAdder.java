package com.example.JUC.U1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Test41_LongAdder {

    public static void main(String[] args){
        demo(
                ()-> new AtomicLong(0),
                (adder)-> adder.getAndIncrement()
        );

        demo(
                () -> new LongAdder(),
                longAdder -> longAdder.increment()
        );
    }

    /**
     *
     * @param adderSupplier     ()->结果   提供累加器对象
     * @param action            (参数)->void  执行累加操作
     * @param <T>
     */
    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action){
        T adder = adderSupplier.get();
        List<Thread> ts = new ArrayList<>();
        //4个线程  每个累加50万
        for (int i = 0; i <4; i++) {
            ts.add(new Thread(()->{
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(thread -> thread.start());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.nanoTime();
        System.out.println("total:"+adder+" cost:"+(end-start)/1000_000);


    }
}
