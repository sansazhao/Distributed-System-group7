package Core;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;



public class LockService {
    static class LockWatcher implements Watcher {
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
    private static Integer key = new Integer(1);
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

    public static String lock(){
        String localLockPath = "";
        String lockPath;
        try {

            CountDownLatch localLatch = new CountDownLatch(1);
            boolean empty;
            if(zookeeper == null) init();
            synchronized (key){
                lockPath = zookeeper.create(lockPrefix, "lock node".getBytes(),
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
                empty = nodePath.equals(children.get(0));


            if (empty) {
                System.out.println(nodePath + " acquire the lock");
            }
            else {
                Integer preValue = Integer.valueOf(lockId) - 1;
                String preId = String.format("%010d", preValue);
                String prePath = lockPrefix + preId;
                System.out.println(lockPath.substring(parentPath.length() + 1) +
                        " is watching " + prePath.substring(parentPath.length() + 1));

                zookeeper.exists(prePath, new LockWatcher(localLatch));

            }

            }
            localLockPath = lockPath;
            if(!empty){
                localLatch.await();
                System.out.println(String.format("%s lock awake",localLockPath));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return localLockPath;
    }

    public static void unlock(String lockpath) {
        try {

            synchronized (key) {
                if(zookeeper == null) init();
                System.out.println(lockpath.substring(parentPath.length() + 1) + " release the lock");
                //latch = new CountDownLatch(1);
                zookeeper.delete(lockpath, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
