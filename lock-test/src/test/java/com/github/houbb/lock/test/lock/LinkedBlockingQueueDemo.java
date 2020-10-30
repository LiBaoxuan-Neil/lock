package com.github.houbb.lock.test.lock;

import java.util.concurrent.*;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class LinkedBlockingQueueDemo {

    private BlockingQueue<String> queue = new LinkedBlockingQueue<>(3);

    public void put(final String put) throws InterruptedException {
        System.out.println("设置开始");
        TimeUnit.SECONDS.sleep(1);
        queue.put(put);
        System.out.println("设置完成: " + put);
    }

    public void take() throws InterruptedException {
        System.out.println("获取开始");
        String take = queue.take();
        System.out.println("获取成功: " + take);
    }

    public static void main(String[] args) {
        final LinkedBlockingQueueDemo queueTest = new LinkedBlockingQueueDemo();
        // 写入线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 3; i++) {
                        queueTest.put(i+"T");
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
