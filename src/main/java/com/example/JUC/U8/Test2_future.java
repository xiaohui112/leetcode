package com.example.JUC.U8;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class Test2_future {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<String> future1 = pool.submit(() -> {
            log.debug("running..1...");
            Thread.sleep(1000);
            log.debug("end...1..");

            return "ok";
        });

        Future<String> future2 = pool.submit(() -> {
            log.debug("running..2...");
            Thread.sleep(1000);
            log.debug("end..2...");

            return "ok";
        });
        Future<String> future3 = pool.submit(() -> {
            log.debug("running..3...");
            Thread.sleep(1000);
            log.debug("end..3...");

            return "ok";
        });

        log.debug("shutdown");
        pool.shutdown();
    }
}
