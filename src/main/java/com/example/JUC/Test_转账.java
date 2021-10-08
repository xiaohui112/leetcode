package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;


@Slf4j(topic = "c.Test_转账")
public class Test_转账 {

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        },"t1");
        Thread t2 = new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //最后看看 两个账户金额 是否正确
        log.debug("a账户金额：{}",a.getMoney());
        log.debug("b账户金额：{}",b.getMoney());
        log.debug("total金额：{}",a.getMoney()+b.getMoney());


    }
    //Random 为线程安全类
    static Random random = new Random();

    public  static int randomAmount(){
        return random.nextInt(100)+1;
    }
}

//账户
class Account{

    private int money;

    public Account(int money) {
        this.money = money;
    }

    public  int getMoney(){
        return money;
    }

    public  void setMoney(int money){
        this.money = money;
    }

    //转账
    public  void transfer(Account target,int amount){
        synchronized(Account.class){
            if(this.money >= amount){
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }

    }
}