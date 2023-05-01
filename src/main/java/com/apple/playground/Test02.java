package com.apple.playground;

public class Test02 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread02();
        t.start();
        Thread.sleep(2); // 暂停1毫秒
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
    }
}

class MyThread02 extends Thread {
    public void run() {
        int n = 0;
        while (! isInterrupted()) {
            n ++;
            System.out.println(n + " hello!");
        }
    }
}