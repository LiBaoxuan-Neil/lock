package com.github.houbb.lock.test;

import java.util.concurrent.Phaser;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class PhaserDemo {

    private static class GameRunnable implements Runnable {

        private final Phaser phaser;

        private GameRunnable(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            //参加上半场比赛
            System.out.println("玩家-"+Thread.currentThread().getName()+":参加上半场比赛");
            //执行这个方法的话会等所有的选手都完成了之后再继续下面的方法
            phaser.arriveAndAwaitAdvance();


            // 下半场
            //参加上半场比赛
            System.out.println("玩家-"+Thread.currentThread().getName()+":参加下半场比赛");
            //执行这个方法的话会等所有的选手都完成了之后再继续下面的方法
            phaser.arriveAndAwaitAdvance();
        }
    }

    public static void main(String[] args) {
        int nums = 3;
        Phaser phaser = new MyPhaser();

        //注册一次表示 phaser 维护的线程个数
        phaser.register();

        for(int i = 0; i < nums; i++) {
            phaser.register();

            Thread thread = new Thread(new GameRunnable(phaser));
            thread.start();
        }

        //后续阶段主线程就不参加了
        phaser.arriveAndDeregister();
    }

}
