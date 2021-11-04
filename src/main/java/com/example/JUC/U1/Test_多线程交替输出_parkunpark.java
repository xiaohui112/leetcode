package com.example.JUC.U1;

import java.util.concurrent.locks.LockSupport;

public class Test_多线程交替输出_parkunpark {
    static Thread a;
    static Thread b;
    static Thread c;

    public static void main(String[] args){
        ParkUnpark parkUnpark = new ParkUnpark(5);
        a = new Thread(()->parkUnpark.print("a",b));
        b = new Thread(()->parkUnpark.print("b",c));
        c = new Thread(()->parkUnpark.print("c",a));
        a.start();
        b.start();
        c.start();

        LockSupport.unpark(a);

    }
}

class ParkUnpark{
    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str,  Thread next){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
}
