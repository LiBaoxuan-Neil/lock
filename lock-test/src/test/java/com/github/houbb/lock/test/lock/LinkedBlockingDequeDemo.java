package com.github.houbb.lock.test.lock;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class LinkedBlockingDequeDemo {

    private static class Producer implements Runnable{
        private BlockingDeque<Integer> queue;
        public Producer(BlockingDeque<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    Integer num = ThreadLocalRandom.current().nextInt(100);
                    queue.put(num);
                    System.out.println(String.format("%s producer a num %d",Thread.currentThread().getName(),num));
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static class Consumer implements Runnable{
        private BlockingDeque<Integer> queue;
        public Consumer(BlockingDeque<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    System.out.println(String.format("%s consume a num %d",Thread.currentThread().getName(),queue.take()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingDeque<Integer> queue = new LinkedBlockingDeque<>(100);
        new Thread(new Producer(queue),"Producer").start();
        new Thread(new Consumer(queue),"Consumer").start();
    }


}
