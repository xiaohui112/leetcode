package com.example.JUC;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test_多线程交替输出_ReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);

        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(()-> awaitSignal.print("a",a,b)).start();
        new Thread(()-> awaitSignal.print("b",b,c)).start();
        new Thread(()-> awaitSignal.print("c",c,a)).start();

        Thread.sleep(1000);
        try{
            awaitSignal.lock();
            a.signal();
        }finally {
            awaitSignal.unlock();
        }
    }

}

class AwaitSignal extends ReentrantLock {
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition current, Condition next){
        for (int i = 0; i < loopNumber; i++) {
            try{
                this.lock();
                current.await();//线程进入当前休息室
                System.out.print(str);//打印
                next.signal(); //唤醒下一个休息室

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }
}
