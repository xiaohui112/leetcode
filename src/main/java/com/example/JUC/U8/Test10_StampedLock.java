package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

@Slf4j(topic = "c.Test10_StampedLock")
public class Test10_StampedLock {

    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(()->{
            dataContainer.read(1000);
        },"t1").start();

        Thread.sleep(500);
        new Thread(()->{
            dataContainer.write(0);
        },"t2").start();


    }
}

@Slf4j(topic = "c.DataContainerStamped")
class DataContainerStamped{
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime){
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking...{}",stamp);
        try {
            Thread.sleep(readTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(lock.validate(stamp)){
            log.debug("read finish...{}",stamp);
            return data;
        }
        log.debug("updating to read lock...{}",stamp);
        try{
            stamp = lock.readLock();
            log.debug("read lock {}",stamp);

            try{
                Thread.sleep(readTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("read finish...{}",stamp);
            return data;


        } finally {
            log.debug("read unlock...{}",stamp);
            lock.unlockRead(stamp);
        }


    }

    public void write(int newData){
        long stamp = lock.writeLock();
        log.debug("write lock {}",stamp);
        try{
            Thread.sleep(2000);
            this.data = newData;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("write unlock {}",stamp);
            lock.unlockWrite(stamp);
        }
        
    }
}