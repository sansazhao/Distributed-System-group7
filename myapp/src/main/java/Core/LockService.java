package Core;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class LockWatcher implements Watcher {
    private CountDownLatch latch;
    LockWatcher(CountDownLatch countDownLatch){
        this.latch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType()== Event.EventType.NodeDeleted){
            System.out.println(event.getPath().substring(6) + " is released");
            latch.countDown();
        }
    }
}

public class LockService {
    private static ZooKeeper zookeeper;
    private static String zookeeper_server;
    private static String parentPath = "/lock";
    private static String lockPrefix = "/lock/lock-";

    static public void init() {
        zookeeper_server = new String("dist-1:2181,dist-2:2181,dist-3:2181");

        try {
            zookeeper = new ZooKeeper("localhost:2181",2000, null);
            Stat stat = zookeeper.exists(parentPath, null);
            if (stat == null) {
                zookeeper.create(parentPath, "for lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void lock(){
        try {
            String lockPath = zookeeper.create(lockPrefix, "lock node".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            String lockId = lockPath.substring(lockPrefix.length()); // 0000000001
            String nodePath = lockPath.substring(parentPath.length() + 1); // lock-0000000001

            List<String> children = zookeeper.getChildren("/lock", false);
            Collections.sort(children, new Comparator<String>() {
                public int compare(String left, String right) {
                    String leftId = left.substring(lockPrefix.length());
                    String rightId = right.substring(lockPrefix.length());
                    return leftId.compareTo(rightId);
                }
            });
            if (nodePath.equals(children.get(0))) {
                System.out.println(nodePath + " acquire the lock");
            }
            else {
                Integer preValue = Integer.valueOf(lockId) - 1;
                String preId = String.format("%010d", preValue);
                String prePath = lockPrefix + preId;
                System.out.println(lockPath.substring(parentPath.length() + 1) +
                        " is watching " + prePath.substring(parentPath.length() + 1));
                CountDownLatch latch = new CountDownLatch(1);
                LockWatcher lockWatcher = new LockWatcher(latch);
                Stat stat = zookeeper.exists(prePath, lockWatcher);
                if (stat != null) {
                    latch.await();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unlock() {
        try {
            List<String> children = zookeeper.getChildren("/lock", false);
            Collections.sort(children, new Comparator<String>() {
                public int compare(String left, String right) {
                    String leftId = left.substring(lockPrefix.length());
                    String rightId = right.substring(lockPrefix.length());
                    return leftId.compareTo(rightId);
                }
            });
            String lockPath = children.get(0);
            System.out.println(lockPath + " release the lock");
            zookeeper.delete(parentPath + "/" + lockPath, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
