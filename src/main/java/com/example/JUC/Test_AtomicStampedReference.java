package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j(topic = "c")
public class Test_AtomicStampedReference {

    public static void main(String[] args) throws InterruptedException {
        AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);

        log.debug("main start...");
        String prev = ref.getReference();
        int mstamp = ref.getStamp();
        log.debug("版本号为:{}",mstamp);

        new Thread(()->{
            //每次都得在上一版本的基础上更新，更新成功版本+1
            int stamp  = ref.getStamp();
            log.debug("change A->B {}",ref.compareAndSet("A","B",stamp,stamp+1));

            //
            log.debug("do sth unknown...");

            //每次都得在上一个版本的基础上更新，更新成功版本+1
            stamp = ref.getStamp();
            log.debug("change B->A {}",ref.compareAndSet("B","A",stamp,stamp+1));
        }).start();

        Thread.sleep(1000);

        //主线程修改
        log.debug("change A->C {}",ref.compareAndSet(prev,"C",mstamp,mstamp+1));
    }
}
