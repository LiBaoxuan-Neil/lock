package com.github.houbb.lock.test.lock;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class PriorityBlockingQueueDemo {

    private static class User implements Comparable<User> {

        private final int order;

        private final String name;

        private User(int order, String name) {
            this.order = order;
            this.name = name;
        }

        @Override
        public int compareTo(User o) {
            return this.order - o.order;
        }

        @Override
        public String toString() {
            return "User{" +
                    "order=" + order +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private PriorityBlockingQueue<User> queue = new PriorityBlockingQueue<>();

    public void put(final User user) throws InterruptedException {
        System.out.println("设置开始");
        queue.put(user);
        System.out.println("设置完成: " + user);
    }

    public void take() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("获取开始");
        User take = queue.take();
        System.out.println("获取成功: " + take);
    }

    public static void main(String[] args) {
        final PriorityBlockingQueueDemo queueTest = new PriorityBlockingQueueDemo();
        // 写入线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 5; i++) {
                        int order = ThreadLocalRandom.current().nextInt(10);
                        User user = new User(order, i+"-user");
                        queueTest.put(user);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 读取线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        queueTest.take();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
