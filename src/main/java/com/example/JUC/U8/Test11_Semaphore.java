package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j(topic = "c.Test11_Semaphore")
public class Test11_Semaphore {

    /**
     * 信号量，限制并发数
     * @param args
     */
    public static void main(String[] args){
        Semaphore s = new Semaphore(3);

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try{
                  s.acquire();//获取信号量
                    log.debug("我是线程---{}",Thread.currentThread().getName());
                    Thread.sleep(2000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    s.release();//释放信号量
                }
            }).start();


            new Thread();

        }



    }
}
