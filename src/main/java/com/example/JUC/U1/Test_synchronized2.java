package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test_synchronized2")
public class Test_synchronized2 {


    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread t1 = new Thread(()->{
            for(int i = 0; i < 5000; ++i){
                room.increment();
            }
        });

        
        Thread t2 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        });
        
        t1.start();
        t2.start();
        //等待他t1 t2 执行完
        t1.join();
        t2.join();

        log.debug("count = {}",room.getCounter());
    }


    static class Room{
        private int counter;

        public void increment(){
            synchronized (this){
                counter++;
                log.debug("+++++++++:{}",counter);
            }
        }

        public synchronized void decrement(){
                counter--;
                log.debug("--------:{}",counter);
        }

        public synchronized int getCounter(){
                return counter;
        }
    }
}
