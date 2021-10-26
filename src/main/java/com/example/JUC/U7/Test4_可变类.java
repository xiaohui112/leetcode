package com.example.JUC.U7;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Slf4j
public class Test4_可变类 {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                TemporalAccessor parse = dtf.parse("2021-10-14");
                log.debug("{}",parse);

            }).start();
        }
    }

    private static void test1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                synchronized (sdf){
                    try {
                        log.debug("{}",sdf.parse("2021-10-14"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }
    }
}
