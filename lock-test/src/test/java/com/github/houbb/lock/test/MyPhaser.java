package com.github.houbb.lock.test;

import java.util.concurrent.Phaser;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class MyPhaser extends Phaser {

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0 :
                System.out.println("上半场完成");
                return false;
            case 1:
                System.out.println("下半场完成");
                return false;
            default:
                return true;
        }
    }

}
