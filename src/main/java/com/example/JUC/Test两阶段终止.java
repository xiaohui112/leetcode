package com.example.JUC;

import lombok.extern.slf4j.Slf4j;

public class Test两阶段终止 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination_volatile twoPhaseTermination = new TwoPhaseTermination_volatile();
        twoPhaseTermination.start();

        Thread.sleep(5000);
        twoPhaseTermination.stop();
    }
}
@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination{
    private Thread mintor;
    public void start(){
        mintor = new Thread(()->{
            while(true){
                Thread thread = Thread.currentThread();
                if(thread.isInterrupted()){
                    log.debug("线程被打断。。。。料理后事");
                    break;
                }else{
                    try {
                        thread.sleep(1000);
                        log.debug("执行监控。。。。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mintor.interrupt();
                        log.debug("重置打断标记，下次循环终止线程");
                    }
                }

            }
        });
        mintor.start();
    }

    public void stop(){
        mintor.interrupt();
    }
}

@Slf4j(topic = "c.volatile")
class TwoPhaseTermination_volatile{
    private Thread mintor;
    private volatile boolean stop = false;
    public void start(){
        mintor = new Thread(()->{
            while(true){
                Thread thread = Thread.currentThread();
                if(stop){
                    log.debug("线程被打断。。。。料理后事");
                    break;
                }else{
                    try {
                        thread.sleep(1000);
                        log.debug("执行监控。。。。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
//                        mintor.interrupt();
                        log.debug("重置打断标记，下次循环终止线程");
                    }
                }

            }
        });
        mintor.start();
    }

    public void stop(){
        stop = true;
        mintor.interrupt();//打断线程睡眠
    }
}