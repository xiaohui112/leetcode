package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j(topic = "c.Test13_GameLoading")
public class Test13_GameLoading {
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Random r = new Random();
        CountDownLatch latch = new CountDownLatch(10);
        String[] all = new String[10];

        for (int j = 0; j <10; j++) {
            int finalJ = j;
            service.submit(() -> {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[finalJ] = i+"%";
                    System.out.print("\r"+ Arrays.toString(all));
                }
            });
            latch.countDown();
        }

        latch.await();
        System.out.println("游戏开始");
        service.shutdown();
    }
}
