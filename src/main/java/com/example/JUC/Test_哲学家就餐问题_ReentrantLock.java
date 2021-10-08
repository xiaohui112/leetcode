package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

public class Test_哲学家就餐问题_ReentrantLock {
    public static void main(String[] args){
        Chopstick_ReentrantLock c1 = new Chopstick_ReentrantLock("c1");
        Chopstick_ReentrantLock c2 = new Chopstick_ReentrantLock("c2");
        Chopstick_ReentrantLock c3 = new Chopstick_ReentrantLock("c3");
        Chopstick_ReentrantLock c4 = new Chopstick_ReentrantLock("c4");
        Chopstick_ReentrantLock c5 = new Chopstick_ReentrantLock("c5");

        new Philosopher_ReentrantLock("苏格拉底",c1,c2).start();
        new Philosopher_ReentrantLock("柏拉图",c2,c3).start();
        new Philosopher_ReentrantLock("亚里士多德",c3,c4).start();
        new Philosopher_ReentrantLock("赫拉克拉克",c4,c5).start();
        new Philosopher_ReentrantLock("阿基米德",c5,c1).start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher_ReentrantLock extends Thread{
    Chopstick_ReentrantLock left;
    Chopstick_ReentrantLock right;

    public Philosopher_ReentrantLock(String name,Chopstick_ReentrantLock left, Chopstick_ReentrantLock right){
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while(true){
            /*synchronized (left){
                synchronized (right){
                    log.debug("eating....");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }*/
            if(left.tryLock()){
                try{
                    if(right.tryLock()){
                        try{
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }
                }finally {
                    left.unlock();
                }
            }
        }
    }

    private void eat() {
        log.debug("eating.....");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chopstick_ReentrantLock extends ReentrantLock {

    private String name;

    public Chopstick_ReentrantLock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{"+name+"}";
    }
}