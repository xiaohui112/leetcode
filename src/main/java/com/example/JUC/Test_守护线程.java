package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class Test_守护线程 {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            while(true){
                log.debug("守护线程,,.......");
                if(Thread.currentThread().isInterrupted()){
                    log.debug("守护线程结束、、、、、、、、、");
                    break;
                }
            }
        });
        t1.setDaemon(true);
        t1.start();


        log.debug("主线程结束");
//        t1.interrupt();
    }
}
