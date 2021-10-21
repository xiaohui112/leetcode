package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j(topic = "c.Test3_Starvation")
public class Test3_Starvation {

    static final List<String> MENU = Arrays.asList("地三鲜","宫保鸡丁","辣子鸡","烤鸭");
    static Random random = new Random();
    static String cooking(){
        return MENU.get(random.nextInt(MENU.size()));
    }

    public static void main(String[] args){
        /**
         * 两个线程的线程池，同时创建两个任务，会造成饥饿。导致任务无法完成
         *
         * 解决方法：
         * 不同的任务交给不同的线程池处理
         */
//        ExecutorService pool = Executors.newFixedThreadPool(2);
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);



        waiterPool.execute(()->{
            log.debug("服务员，点餐.......");
            Future<String> f = cookPool.submit(() -> {
                log.debug("大厨做饭");
                return cooking();
            });

            try {
                log.debug("菜来了，客观慢用，{}",f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        waiterPool.execute(()->{
            log.debug("服务员，点餐.......");
            Future<String> f = cookPool.submit(() -> {
                log.debug("大厨做饭");
                return cooking();
            });

            try {
                log.debug("菜来了，客观慢用，{}",f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}
