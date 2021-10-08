package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j(topic = "c.Test_卖票")
public class Test_卖票 {

    public static void main(String[] args) throws InterruptedException {
        TicketWindow window = new TicketWindow(100);

        List<Thread> threadList = new ArrayList<>();
        List<Integer> amountList = new Vector<>();

        for (int i = 0; i < 5000; i++) {
            Thread thread = new Thread(() -> {
                //买票
                int amount = window.sell(randomAmount());
                try {
                    Thread.sleep(randomAmount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                amountList.add(amount);
            });
            threadList.add(thread);
            thread.start();
        }

        //等待全部线程执行完成
        for (Thread thread : threadList) {
            thread.join();
        }

        log.debug("余票：{}",window.getCount());
        log.debug("已售票：{}",amountList.stream().mapToInt(Integer::intValue).sum());

    }

    //Random 为线程安全类
    static Random random = new Random();

    public  static int randomAmount(){
        return random.nextInt(5)+1;
    }

}

class TicketWindow{
    private int count;
    public TicketWindow(int count){
        this.count = count;
    }

    //查询余票
    public int getCount(){
        return count;
    }

    //售票
    public synchronized int sell(int amount){
        if(this.count >= amount){
            this.count -= amount;
            return amount;
        }else{
            return 0;
        }
    }
}