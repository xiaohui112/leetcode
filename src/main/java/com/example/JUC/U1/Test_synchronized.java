package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test_synchronized")
public class Test_synchronized {

    private static int count = 0;
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            for(int i = 0; i < 5000; ++i){
                synchronized (lock){
                    count ++;
                }
            }
        });

        
        Thread t2 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (lock){
                    count --;
                }
            }
        });
        
        t1.start();
        t2.start();
        //等待他t1 t2 执行完
        t1.join();
        t2.join();

        log.debug("count = {}",count);
    }


}
