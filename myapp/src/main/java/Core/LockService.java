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

    static public void init() {
        zookeeper_server = new String("dist-1:2181,dist-2:2181,dist-3:2181");

        try {
            zookeeper = new ZooKeeper(zookeeper_server,2000, null);
            Stat stat = zookeeper.exists("/lock", null);
            if (stat == null) {
                zookeeper.create("/lock", "for lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            initCommodity();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void initCommodity() {
        List<Integer> ids = CommodityService.getCommodityName();
        for(Integer id : ids) {
            try {
                Stat stat = zookeeper.exists("/lock/" + id, null);
                if (stat == null) {
                    zookeeper.create("/lock/" + id, (id + " lock").getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String lock(Integer id){
        String localLockPath = "";
        String parentPath = "/lock/" + id.toString();
        String lockPrefix = "/lock/" + id.toString() + "/lock-";
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
                List<String> children = zookeeper.getChildren(parentPath, false);
                Collections.sort(children, new Comparator<String>() {
                    public int compare(String left, String right) {
                        System.out.println(left);
                        System.out.println(right);
                        String leftId = left.substring("lock-".length());
                        String rightId = right.substring("lock-".length());
                        System.out.println(leftId);
                        System.out.println(rightId);
                        return leftId.compareTo(rightId);
                    }
                });
                empty = nodePath.equals(children.get(0));


            if (empty) {
                System.out.println(id.toString() + "/" + nodePath + " acquire the lock");
            }
            else {
                Integer preValue = Integer.valueOf(lockId) - 1;
                String preId = String.format("%010d", preValue);
                String prePath = lockPrefix + preId;
                System.out.println(lockPath.substring("/lock/".length()) +
                        " is watching " + prePath.substring("/lock/".length()));

                zookeeper.exists(prePath, new LockWatcher(localLatch));

            }

            }
            localLockPath = lockPath;
            if(!empty){
                localLatch.await();
                System.out.println(String.format("%s lock awake",localLockPath.substring("/lock/".length())));
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
                System.out.println(lockpath.substring("/lock".length() + 1) + " release the lock");
                //latch = new CountDownLatch(1);
                zookeeper.delete(lockpath, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
