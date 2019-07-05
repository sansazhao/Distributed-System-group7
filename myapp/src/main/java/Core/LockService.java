package Core;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

class LockWatcher implements Watcher {
    CountDownLatch latch;
    LockWatcher(CountDownLatch countDownLatch){
        this.latch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType()== Event.EventType.NodeDeleted){//当前节点是否删除
            latch.countDown();
        }
    }
}

public class LockService {
    static ZooKeeper zookeeper;
    static String zookeeper_server;
    static CountDownLatch latch = new CountDownLatch(1);
    static public void init() {
        zookeeper_server = new String("dist-1:2181,dist-2:2181,dist-3:2181");


        try {
            zookeeper = new ZooKeeper(zookeeper_server,2000,null);
            zookeeper.create("/lock/", "for lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static public void lock(){


    }

    static public void unlock() {

    }
}
