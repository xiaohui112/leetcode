package com.example.JUC.U8;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j(topic = "c.Test7_AQS")
public class Test7_AQS {
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(()->{
            lock.lock();
            try{
                log.debug("locking....");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking....");
                lock.unlock();
            }
        },"t1").start();

        new Thread(()->{
            lock.lock();
            try{
                log.debug("locking....");
            }finally {
                log.debug("unlocking....");
                lock.unlock();
            }
        },"t2").start();
    }
}


//自定义所（不可重入锁）
@Slf4j(topic = "c.MyLock")
class MyLock implements Lock {

    class MySync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition(){
            return new ConditionObject();
        }

    }

    private MySync sync = new MySync();
    //获取锁 不可打断
    @Override
    public void lock() {
        sync.acquire(1);
    }

    //获取可打断锁
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //尝试获取锁
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    //待超时的 尝试获取锁
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    //解锁
    @Override
    public void unlock() {
        sync.release(1);
    }

    //条件变量
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
