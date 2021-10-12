package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j(topic = "c.Test_AtomicReference")
public class Test_AtomicReference {
    public static void main(String[] args){
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }
}

class DecimalAccountCas implements DecimalAccount{
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance){
        this.balance = new AtomicReference<>(balance);
    }


    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while(true){
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if(balance.compareAndSet(prev,next)){
                break;
            }
        }
    }
}

interface DecimalAccount{
    //获取余额
    BigDecimal getBalance();
    //取款
    void withdraw(BigDecimal amount);

    /**
     * 方法内会启动1000个线程，每个下次线程-10元的操作
     * 如果初始余额为10000。那么正确的结果应该是0
     */

    static void demo(DecimalAccount account){
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(()->{
                account.withdraw(BigDecimal.TEN);
            }));
        }
        long start = System.currentTimeMillis();
        ts.forEach(Thread::start);
        ts.forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();

        System.out.println("最后余额："+ account.getBalance()+";耗时: "+(end-start));
    }
}