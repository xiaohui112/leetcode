package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j(topic = "c.Test4_Schedule")
public class Test4_Schedule {
    
    //如何仍每周四 18：00：00 定时执行任务
    public static void main(String[] args){

        //当前时间
        LocalDateTime now = LocalDateTime.now();
        log.debug("当前时间：{}",now);
        //本周四18:00
        LocalDateTime time = now.withHour(15).withMinute(57).withSecond(0).withNano(0).with(DayOfWeek.WEDNESDAY);
        //如果当前时间超过本周四18点，取下周四18点
        if(now.compareTo(time)>0){
            time = time.plusWeeks(1);
        }
        log.debug("执行时间：{}",time);
        long initialDelay = Duration.between(now, time).toMillis();  //当前时间 和 周四18点的差值
        //***** 这里如果是负数，会立即执行
        log.debug("initialDelay：{}",initialDelay);
        long period = 1000; //* 60 * 60 * 24 * 7;//时间间隔
        //任务调度线程池
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        pool.scheduleAtFixedRate(()->{
            log.debug("I'm working.....");
        },initialDelay,period, TimeUnit.MILLISECONDS);
    }

}
