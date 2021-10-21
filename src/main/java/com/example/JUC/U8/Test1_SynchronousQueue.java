package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;

@Slf4j(topic = "c.Test1_SynchronousQueue")
public class Test1_SynchronousQueue {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(()->{
            try {
                log.debug("putting {}",1);
                integers.put(1);
                log.debug("{},putted...",1);

                log.debug("putting {}",2);
                integers.put(2);
                log.debug("{},putted...",2);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        Thread.sleep(1000);

        new Thread(()->{
            try {
                Integer take = integers.take();
                log.debug("taking {}",take);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();

        Thread.sleep(1000);

        new Thread(()->{
            try {
                Integer take = integers.take();
                log.debug("taking {}",take);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t3").start();
    }
}
