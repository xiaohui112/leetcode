package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test_reentrantlock_trylock")
public class Test_reentrantlock_trylock {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            log.debug("尝试获取锁");
            try {
//                if(!lock.tryLock()){
                if(!lock.tryLock(2, TimeUnit.SECONDS)){
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁");
                return;
            }

            try{
                log.debug("获取到锁");
            }finally {
                lock.unlock();
            }
        },"t1");

        lock.lock();
        log.debug("获取到锁");
        t1.start();
        Thread.sleep(1000);
        lock.unlock();
        log.debug("释放锁");

    }
}
