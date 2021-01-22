package kademlia.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/22
 */
public class CountableThreadPool {

    private final int threadNum;

    private final AtomicInteger threadAlive = new AtomicInteger();

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private final ExecutorService executorService;

    public CountableThreadPool(int threadNum){
        this.threadNum = threadNum;
        this.executorService = Executors.newCachedThreadPool();
    }

    public CountableThreadPool(int threadNum,ExecutorService executorService){
        this.threadNum = threadNum;
        this.executorService = executorService;
    }

    public int getThreadNum(){
        return threadNum;
    }

    public int getThreadAlive(){
        return threadAlive.get();
    }
    public void execute(final Runnable runnable){
        execute1(runnable);
    }

    private void execute1(Runnable runnable) {
        if (threadAlive.get() >= threadNum){
            try{
                lock.lock();
                while(threadAlive.get() >= threadNum){
                    try{
                        condition.await();
                    } catch (InterruptedException ignored) {
                    }
                }
            }finally {
                lock.unlock();
            }
        }
        threadAlive.incrementAndGet();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    runnable.run();
                }finally {
                    try{
                        lock.lock();
                        threadAlive.decrementAndGet();
                        condition.signal();
                    }finally{
                        lock.unlock();
                    }
                }
            }
        });
    }

    public <T> Future<T> submit(Callable<T> task){
        return executorService.submit(task);
    }
    public boolean isShutdown(){
        return executorService.isShutdown();
    }

    public void shutdown(){
        executorService.shutdown();
    }

}
