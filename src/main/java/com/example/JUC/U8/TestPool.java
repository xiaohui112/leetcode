package com.example.JUC.U8;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestPool")
public class TestPool {
//

    public static void main(String[] args){
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MICROSECONDS, 1,
                (queue,task)->{

                    //队列满了的 处理策略
                    //1.死等
//                    queue.put(task);
                    //2.超时等待
//                    queue.offset(task,1200,TimeUnit.MICROSECONDS);
                    //3.调用者放弃线程执行
//                    log.debug("队列已满，放弃任务",task);
                    //4.调用者抛出异常
//                    throw new RuntimeException("任务执行失败。"+task);
                    //5.让调用者自己执行任务
                    task.run();
                });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            threadPool.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("任务：{}", finalI);
            });
        }
    }
}

//拒绝策略
@FunctionalInterface
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue,T task);
}

//线程池类
@Slf4j(topic = "c.ThreadPool")
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;

    //线程集合
    private HashSet<Worker> workers = new HashSet<>();

    //核心线程数
    private int coreSize;

    //获取任务超时时间
    private long timeout;

    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout , TimeUnit timeUnit, int queueCapcity,RejectPolicy<Runnable> rejectPolicy){
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.rejectPolicy = rejectPolicy;
    }

    //执行任务
    public void execute(Runnable task){
        //当任务数量没有超过coresize时，直接交给worker对象执行
        //当任务数超过coresize时，加入任务队列暂存
        synchronized (workers){
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增workers:{},task:{}",worker,task);
                workers.add(worker);
                worker.start();
            }else{
                /*log.debug("加入队列,task:{}",task);
                taskQueue.put(task);*/

                taskQueue.tryPut(rejectPolicy,task);
            }
        }
    }


    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task){
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //1.当任务不为空，执行任务
            //2.当任务执行完毕，再接着从任务队列获取任务并执行
            while (task != null || (task = taskQueue.poll(timeout,timeUnit))!=null){
                try{
                    log.debug("正在执行...{}",task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            synchronized (workers){
                log.debug("workers 被移除{}",this);
                workers.remove(this);
            }
        }
    }
}

@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T>{
    //1.任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //2.锁
    private ReentrantLock lock = new ReentrantLock();
    //3.生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4.消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    //5.队列容量
    private int capcity;

    //待超时的阻塞获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try{
            long nanos = unit.toNanos(timeout);
            while(queue.isEmpty()){
                try {
                    //nanos 不断减小，<0就不在等待，返回空
                    if(nanos <= 0){
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }


    //阻塞获取任务
    public T take(){
        lock.lock();
        try{
            while(queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }

    //阻塞添加任务
    public void put(T element){
        lock.lock();
        try{
            while(queue.size() == capcity){
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(element);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }
    //待超时时间 阻塞添加任务
    public boolean offset(T element,long timeout,TimeUnit timeUnit){
        lock.lock();
        try{
            long nanos = timeUnit.toNanos(timeout);
            while(queue.size() == capcity){
                try {
                    if(nanos <= 0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(element);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    public int size(){
        lock.lock();
        try{
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    //带拒绝策略的添加任务
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try{
            if(queue.size() == capcity){
                log.debug("执行决绝策略的方法....");
                rejectPolicy.reject(this,task);
            }else{
                log.debug("加入任务队列{},",task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }

        }finally {
            lock.unlock();
        }
    }
}
