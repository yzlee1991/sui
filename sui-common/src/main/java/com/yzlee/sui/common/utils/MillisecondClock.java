package com.yzlee.sui.common.utils;

import java.util.concurrent.ExecutorService;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:02
 */
//优化性能，降低精度，减少多线程获取系统时间的损耗
public class MillisecondClock {

    private long rate = 10;// 频率

    private volatile long now = 0;// 当前时间

    private ExecutorService cachedThreadPool;

    public MillisecondClock(ExecutorService cachedThreadPool) {

        this.cachedThreadPool = cachedThreadPool;

        this.now = System.currentTimeMillis();

        start();
    }

    public MillisecondClock(ExecutorService cachedThreadPool, long rate) {

        this.cachedThreadPool = cachedThreadPool;

        this.rate = rate;

        this.now = System.currentTimeMillis();

        start();
    }

    private void start() {
        cachedThreadPool.execute(() -> {
            while(true){
                try {
                    Thread.sleep(rate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                now = System.currentTimeMillis();
            }
        });
    }

    public long now() {
        return now;
    }

}
