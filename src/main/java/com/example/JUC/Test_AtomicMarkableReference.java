package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Slf4j(topic = "c")
public class Test_AtomicMarkableReference {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        //参数2 mark 可以看做一个标记，表示垃圾袋满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag,true);

        log.debug("start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());

        //保洁阿姨
        new Thread(()->{
            log.debug("start....");
            bag.setDesc("空垃圾袋");
            ref.compareAndSet(bag,bag,true,false);
            log.debug(bag.toString());
        },"保洁阿姨").start();


        Thread.sleep(1000);
        log.debug("想换一个新的垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"),true ,false);
        log.debug("换了吗？"+success);
        log.debug(ref.getReference().toString());

    }
}

class GarbageBag{
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return super.toString()+" "+desc;
    }
}