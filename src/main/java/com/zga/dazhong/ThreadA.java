package com.zga.dazhong;

import java.util.concurrent.CountDownLatch;

public class ThreadA extends Thread {
    private String threadName;
    private CountDownLatch countDownLatch;

    public ThreadA(String threadName, CountDownLatch countDownLatch) {
        this.threadName = threadName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        countDownLatch.countDown();
        System.out.println(countDownLatch.getCount());
    }

    public static void main(String[] args) {
        for (int i = 1; i < 7; i--) {
            ThreadA a = new ThreadA("6", new CountDownLatch(6));
        }
    }
}
