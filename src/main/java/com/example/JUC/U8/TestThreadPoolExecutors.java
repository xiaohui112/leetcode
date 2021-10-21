package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "c.TestThreadPoolExecutors")
public class TestThreadPoolExecutors {

    public static void main(String[] args){
//        ExecutorService pool = Executors.newFixedThreadPool(2);
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"mypool_t"+t.getAndIncrement());
            }
        });

        pool.execute(()->{
            log.debug("thread 1");
        });
        pool.execute(()->{
            log.debug("thread 2");
        });
        pool.execute(()->{
            log.debug("thread 3");
        });
    }
}
