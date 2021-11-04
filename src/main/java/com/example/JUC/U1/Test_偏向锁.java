package com.example.JUC.U1;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.Test_偏向锁")
public class Test_偏向锁 {

    /**
            |--------------------------------------------------------------------|------------------|
            |                            Mark word (64 bits)                     |    State         |
            |--------------------------------------------------------------------|------------------|
            |--unused:25--|hashcode:32|unused:1|age:4|biased_lock:1|lock_state:01|   Normal         |      无锁状态
            |--------------------------------------------------------------------|------------------|
            |  thread:54  |  epoch:2  |unused:1|age:4|biased_lock:1|lock_state:01|   Biased         |       偏向锁
            |--------------------------------------------------------------------|------------------|
            |                   ptr_to_lock_record:62               |     00     |Lightweight Locked|       轻量级锁
            |--------------------------------------------------------------------|------------------|
            |                   ptr_to_heavyweiight_monitor         |     10     |Heavyweighr Locked|       重量级锁
            |--------------------------------------------------------------------|------------------|
            |                                                       |     11     |mark for GC       |       垃圾回收
            |--------------------------------------------------------------------|------------------|
            |
     */

    public static void main(String[] args){
        String s = new String();
        //打印对象头
        log.debug(ClassLayout.parseInstance(s).toPrintable());
        //64为系统
        // 00000000 00000000 00000000 00000000 00011111 11101011 11010000 00000101
    }
}
