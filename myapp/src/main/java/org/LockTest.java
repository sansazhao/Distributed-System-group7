package org;

import Core.LockService;

public class LockTest {
    public static void main(String[] args) {
        LockService.init();
        for (int i = 0; i < 10; i++) {
            new MyThread().start();
        }
    }

    private static class MyThread extends Thread {
        public void run() {
            LockService.lock();
            LockService.unlock();
        }
    }
}
