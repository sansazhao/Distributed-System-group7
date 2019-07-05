package org;

import Core.LockService;
import org.apache.hadoop.fs.Stat;
import org.apache.hadoop.fs.shell.Count;
import org.apache.zookeeper.*;
import sun.java2d.SurfaceDataProxy;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class SimpleLock {
    ZooKeeper zookeeper;
    CountDownLatch latch;
    String path;
    public SimpleLock(String Path,String zookeeper_server) {
        latch = new CountDownLatch(1);
        this.path = Path;
        try {
            zookeeper = new ZooKeeper(zookeeper_server, 2000, new LockWatcher(latch));
            if (zookeeper.exists("/lock", false) != null)
                zookeeper.create("/lock", "for lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LockWatcher implements Watcher {
        CountDownLatch latch;
        LockWatcher(CountDownLatch countDownLatch) {
            latch = countDownLatch;
        }

        @Override
        public void process(WatchedEvent event) {
            if(event.getType() == Event.EventType.NodeDeleted) {
                latch.countDown();
            }
        }
    }
    public void lock() throws Exception{

        while(true){
        org.apache.zookeeper.data.Stat stat = zookeeper.exists("/lock/" + path,true);
        if(stat != null) {
            latch.await();
            latch = new CountDownLatch(1);
        }

        try {
            zookeeper.create("/lock/" + path, "lock".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return;
        }catch (Exception e) {

        }
        }

    }

    public void unlock() throws Exception{
        zookeeper.delete("/lock/" + path,-1);
    }
}

