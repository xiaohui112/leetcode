package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j(topic = "c.Test_ReentrantReadWriteLock")
public class Test_ReentrantReadWriteLock {

    public static void main(String[] args){
        DataContainer data = new DataContainer();

        new Thread(()->{
            data.read();
        },"t1").start();


        new Thread(()->{
            data.write();
        },"t2").start();
    }
}

@Slf4j(topic = "c.DataContainer")
class DataContainer{

    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();


    public Object read(){
        log.debug("获取读取锁...");
        r.lock();
        try{
            log.debug("开始读取...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        } finally {
            log.debug("释放读取锁...");
            r.unlock();
        }
    }

    public void write(){
        log.debug("获取写锁...");
        w.lock();
        try{
            log.debug("写入...");

        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}
