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

@Slf4j(topic = "c.CacheData")
class CacheData{
    Object data;
    //是否有效，如果失败，需要重新计算data
    volatile boolean cacheValid;
    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    void processCachedData(){
        rwl.readLock().lock();
        if(!cacheValid){
            //获取写锁前，必须释放读锁
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try{
                //判断是否有其他线程已经获取了写锁，更新了缓存，避免重复更新
                if(!cacheValid){
                    data = new Object();
                    cacheValid = true;
                }
                //降级为读锁，释放写锁，这样能够让其他线程读取缓存
                rwl.readLock().lock();
            }finally {
                rwl.writeLock().unlock();
            }
        }
        //自己用完数据，释放读锁
        try{
            //use(data);
        }finally {
            rwl.readLock().unlock();
        }
    }
}

