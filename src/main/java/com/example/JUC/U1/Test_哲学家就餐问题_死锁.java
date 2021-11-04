package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

public class Test_哲学家就餐问题_死锁 {
    public static void main(String[] args){
        Chopstick c1 = new Chopstick("c1");
        Chopstick c2 = new Chopstick("c2");
        Chopstick c3 = new Chopstick("c3");
        Chopstick c4 = new Chopstick("c4");
        Chopstick c5 = new Chopstick("c5");

        new Philosopher("苏格拉底",c1,c2).start();
        new Philosopher("柏拉图",c2,c3).start();
        new Philosopher("亚里士多德",c3,c4).start();
        new Philosopher("赫拉克拉克",c4,c5).start();
        new Philosopher("阿基米德",c5,c1).start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher(String name,Chopstick left, Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while(true){
            synchronized (left){
                synchronized (right){
                    log.debug("eating....");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

class Chopstick{

    private String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{"+name+"}";
    }
}