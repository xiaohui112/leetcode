package com.example.JUC;

public class Test_多线程交替输出_synchronized {
    /**
     * 三个线程交替打印  abcabcabcabcabcabc
     */
    public static void main(String[] args){
        Waitnotify wn = new Waitnotify(1,5);

        new Thread(()->{ wn.print("a",1,2); }).start();
        new Thread(()->{ wn.print("b",2,3); }).start();
        new Thread(()->{ wn.print("c",3,1); }).start();
    }
}

class Waitnotify{

    //共用标记位
    private int flag;
    //循环次数
    private int loopNumber;

    public Waitnotify(int flag, int loopNumber){
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(String str, int nowFlag, int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while(this.flag != nowFlag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.print(str);//打印
                this.flag = nextFlag;//修改标记位
                this.notifyAll();//唤醒所有等待线程
            }
        }

    }

}
