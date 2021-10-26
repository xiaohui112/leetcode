package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Slf4j(topic = "c.Test6_ForkJoin")
public class Test6_ForkJoin {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        long start = System.currentTimeMillis();
        Integer result = pool.invoke(new MyTask(1000));
        long end = System.currentTimeMillis();

        System.out.println("result = "+result+" , cast = " + (end - start));

        long start2 = System.currentTimeMillis();
        Integer result2 = pool.invoke(new MyTask2(1,1000));
        long end2 = System.currentTimeMillis();

        System.out.println("result = "+result2+" , cast = " + (end2 - start2));
    }
}
@Slf4j(topic = "c.MyTask")
class MyTask extends RecursiveTask<Integer> {

    //线程之间同步等待，效率不高
    private int n;

    public MyTask(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "{" + n + "}";
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
//            log.debug("join() {}", 1);
            return 1;
        }
        //递归思想 任务拆分 fork
        MyTask task = new MyTask(n - 1);
        task.fork();
//        log.debug("fork() {} + {}", n, task);

        //结果合并
        int result = n + task.join();
//        log.debug("join() {} + {} = {}", n, task, result);
        return result;
    }
}
    @Slf4j(topic = "c.MyTask2")
class MyTask2 extends RecursiveTask<Integer>{
    //设置 begin end 按照范围拆分，提高效率
    private int begin;
    private int end;

    public MyTask2(int begin,int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "MyTask{" +
                 begin +"---"+ end +
                '}';
    }

    @Override
    protected Integer compute() {
        //终止条件
        if(begin == end){
//            log.debug("join() {}",begin);
            return begin;
        }
        //终止条件
        if(end-begin == 1){
//            log.debug("join() {} + {} = {}",begin,end,begin+end);
            return begin + end;
        }

        //计算中间值，从中间进行拆分
        int mid = (begin + end)/2;
        //拆成两个任务
        MyTask2 task1 = new MyTask2(begin, mid);
        task1.fork();

        MyTask2 task2 = new MyTask2(mid + 1, end);
        task2.fork();
//        log.debug("fork() {} + {} = ?",task1,task2);


        int result = task1.join() + task2.join();
//        log.debug("join()  {} + {} = {}",task1,task2,result);
        return result;
    }

}
