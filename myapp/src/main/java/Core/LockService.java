package Core;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    private static ZooKeeper zookeeper;
    private static String zookeeper_server;
    private static String parentPath = "/lock";
    static CountDownLatch latch = new CountDownLatch(1);

    static public void init() {
        zookeeper_server = new String("dist-1:2181,dist-2:2181,dist-3:2181");


        try {
            zookeeper = new ZooKeeper("localhost:2181",2000,null);
//            zookeeper.create(parentPath, "for lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void lock(){
        try {
            String lockPath = "/lock/lock-";
            String nodePath = zookeeper.create(lockPath, "lock node".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            String lockId = nodePath.substring(lockPath.length()); // 0000000001
            nodePath = nodePath.substring(parentPath.length() + 1); // lock-0000000001

            List<String> children = zookeeper.getChildren("/lock", false);
            children.add("lock-0000000012");
            Collections.sort(children, new Comparator<String>() {
                public int compare(String left, String right) {
                    String leftId = left.substring(lockPath.length());
                    String rightId = right.substring(lockPath.length());
                    return leftId.compareTo(rightId);
                }
            });
            if (nodePath.equals(children.get(0))) {
                System.out.println(nodePath + " get the lock");
            }
            else {
                Integer preValue = Integer.valueOf(lockId) - 1;
                String preId = String.format("%010d", preValue);
                String prePath = lockPath + preId;
                System.out.println(prePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unlock() {

    }
}
