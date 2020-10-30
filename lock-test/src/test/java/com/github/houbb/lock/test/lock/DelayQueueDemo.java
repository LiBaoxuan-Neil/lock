package com.github.houbb.lock.test.lock;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author binbin.hou
 * @see java.util.concurrent.DelayQueue
 * @since 1.0.0
 */
public class DelayQueueDemo {


    /**
     * 写入线程
     * @author 老马啸西风
     */
    private static class WriteThread extends Thread {

        private final DelayQueue<DelayElem> delayQueue;

        private WriteThread(DelayQueue<DelayElem> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            for(int i = 0; i < 3; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DelayElem element = new DelayElem(1000,i+"test");
                delayQueue.offer(element);
                System.out.println(System.currentTimeMillis() + " 放入元素 " + i);
            }
        }
    }

    /**
     * 读取线程
     * @author 老马啸西风
     */
    private static class ReadThread extends Thread {

        private final DelayQueue<DelayElem> delayQueue;

        private ReadThread(DelayQueue<DelayElem> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    DelayElem element =  delayQueue.take();
                    System.out.println(System.currentTimeMillis() +" 获取元素：" + element);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static class DelayElem implements Delayed {

        /**
         * 延迟时间
         */
        private final long delay;
        /**
         * 到期时间
         */
        private final long expire;
        /**
         * 数据
         */
        private final String msg;

        private DelayElem(long delay, String msg) {
            this.delay = delay;
            this.msg = msg;
            //到期时间 = 当前时间+延迟时间
            this.expire = System.currentTimeMillis() + this.delay;
        }

        /**
         * 需要实现的接口，获得延迟时间
         *
         * 用过期时间-当前时间
         * @param unit 时间单位
         * @return 延迟时间
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
        }

        /**
         * 用于延迟队列内部比较排序
         * <p>
         * 当前时间的延迟时间 - 比较对象的延迟时间
         *
         * @param o 比较对象
         * @return 结果
         */
        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayElem{" +
                    "delay=" + delay +
                    ", expire=" + expire +
                    ", msg='" + msg + '\'' +
                    '}';
        }

    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayElem> delayQueue = new DelayQueue<>();

        new WriteThread(delayQueue).start();
        new ReadThread(delayQueue).start();
    }

}
