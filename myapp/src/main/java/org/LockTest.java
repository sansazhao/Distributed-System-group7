package org;

import Core.LockService;

public class LockTest {
    public static void main(String[] args) {
        LockService.init();
        LockService.lock();
    }
}
