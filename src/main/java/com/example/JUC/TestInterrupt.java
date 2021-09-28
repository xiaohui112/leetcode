package com.example.JUC;

public class TestInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while(true){
                System.out.println("im working...");
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted){
                    System.out.println("im interrupted");
                    break;
                }
            }
        });

        t1.start();
        t1.interrupt();
        t1.join();

        System.out.println("is end");

    }
}
