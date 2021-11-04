package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c")
public class Test_reentrantLock_condition {
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;


    static ReentrantLock ROOM = new ReentrantLock();
    static Condition cigaretteWaitSet = ROOM.newCondition();
    static Condition takeoutWaitSet = ROOM.newCondition();


    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            ROOM.lock();
            try{
                log.debug("有烟没？？？【{}】",hasCigarette);
                while(!hasCigarette){
                    log.debug("没烟，先歇会");
                    try {
                        cigaretteWaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.debug("有烟了，开始干活！！！！");
            }finally {
                ROOM.unlock();
            }
        },"小南").start();

        new Thread(()->{
            ROOM.lock();
            try{
                log.debug("有外卖没？？？【{}】",hasTakeout);
                while(!hasTakeout){
                    log.debug("没外卖，先歇会");
                    try {
                        takeoutWaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.debug("有外卖了，开始干活！！！！");
            }finally {
                ROOM.unlock();
            }
        },"小女").start();


        Thread.sleep(1000);
        //送烟线程
        new Thread(()->{
            ROOM.lock();
            try{
                hasCigarette = true;
                cigaretteWaitSet.signal();
                log.debug("您的烟到了，请尽情享用！！！");
            }finally {
                ROOM.unlock();
            }
        },"闪送").start();

        Thread.sleep(1000);
        //送外卖线程
        new Thread(()->{
            ROOM.lock();
            try{
                hasTakeout = true;
                takeoutWaitSet.signal();
                log.debug("您的外卖到了，5星好评哦！！！");
            }finally {
                ROOM.unlock();
            }
        },"美团").start();
    }
}
