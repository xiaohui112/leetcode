package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j(topic = "c.Test12_CountDownLatch")
public class Test12_CountDownLatch {
    
    public static void main(String[] args) throws InterruptedException {

    }

    private static void test4() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            log.debug("begin....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end....");
            latch.countDown();
        }).start();

        new Thread(() -> {
            log.debug("begin....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end....");
            latch.countDown();
        }).start();

        new Thread(() -> {
            log.debug("begin....");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end....");
            latch.countDown();
        }).start();

        log.debug("waiting.....");
        latch.await();
        log.debug("wait...end..");
    }
}
