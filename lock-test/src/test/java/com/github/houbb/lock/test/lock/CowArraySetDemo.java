package com.github.houbb.lock.test.lock;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class CowArraySetDemo {

    /**
     * 读线程
     */
    private static class ReadTask implements Runnable {
        Set<String> set;

        public ReadTask(Set<String> set) {
            this.set = set;
        }

        public void run() {
            System.out.println(set);
        }
    }
    /**
     * 写线程
     */
    private static class WriteTask implements Runnable {
        private Set<String> set;
        private String value;

        public WriteTask(Set<String> set, String value) {
            this.set = set;
            this.value = value;
        }

        public void run() {
            set.remove(value);
        }
    }

    public static void main(String[] args) {
        final int NUM = 5;
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < NUM; i++) {
            set.add("main_" + i);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(NUM);
        for (int i = 0; i < NUM; i++) {
            executorService.execute(new WriteTask(set, "main_" + i));
            executorService.execute(new ReadTask(set));
        }
        executorService.shutdown();
    }

}
