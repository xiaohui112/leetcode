package com.example.JUC.U1;

public class Test {


    public static void main(String[] args){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程内输出111111111111");
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

}
