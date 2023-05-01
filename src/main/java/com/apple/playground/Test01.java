package com.apple.playground;

public class Test01 {
    public static void main(String[] args) {
        Thread t1 = new MyThread();
        t1.start();

        Thread t2 = new Thread(new MyRunnable());
        t2.start();

        Thread t3 = new Thread(() -> {
            System.out.println("start new thread 3!");
        });
        t3.start();
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("start new thread 1!");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("start new thread 2!");
    }
}