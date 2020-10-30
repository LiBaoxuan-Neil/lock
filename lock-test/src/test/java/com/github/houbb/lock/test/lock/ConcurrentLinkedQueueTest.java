package com.github.houbb.lock.test.lock;

import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class ConcurrentLinkedQueueTest {

    @Test
    public void helloTest() {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        // add()  将指定元素插入此队列的尾部。
        queue.add("add");

        // offer()  将指定元素插入此队列的尾部。
        queue.offer("offer");

        // peek() 获取但不移除此队列的头；如果此队列为空，则返回 null
        String value = queue.peek();
        System.out.println("PEEK: " + value);

        // poll() 获取并移除此队列的头，如果此队列为空，则返回 null。
        String poll = queue.poll();
        System.out.println("POLL: " + poll);

        // remove() 移除 从队列中移除指定元素的单个实例（如果存在）。
        boolean remove = queue.remove("offer");
        System.out.println("Remove result: " + remove);
    }

}
