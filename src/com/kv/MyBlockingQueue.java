package com.kv;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tanjunzhao on 2021/6/18.
 */
public class MyBlockingQueue<T> {

    private int size;

    private LinkedList<T> list = new LinkedList<>();

    private Lock lock = new ReentrantLock(true);

    private Condition isNotEmpty = lock.newCondition();//队列为空等待条件

    private Condition isNotFull = lock.newCondition();//队列满等待条件

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public void enqueue(T t) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() == size) isNotFull.await();  // 队列已满，等待
            list.add(t);
            System.out.println("入队列 " + t);
            isNotEmpty.signal();    //通知在isNotEmpty条件等待的线程
        } finally {
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        T t;
        lock.lock();
        try {
            while (list.size() == 0) isNotEmpty.await();
            t = list.removeFirst();
            System.out.println("出队列 " + t);
            isNotFull.signal();    //通知在isNotFull条件等待的线程
        } finally {
            lock.unlock();
        }
        return t;
    }
}
