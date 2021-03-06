package Core;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import avro.shaded.com.google.common.primitives.Bytes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Current {
    static ZooKeeper zookeeper;

    static public void connectZookeeper() {
        String zookeeper_servers = new String("dist-1:2181,dist-2:2181,dist-3:2181");

        try {
            zookeeper = new ZooKeeper(zookeeper_servers, 2000, null);
            if(zookeeper.exists("/current", false) == null) zookeeper.create("/current", "for current".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/current/RMB", false) == null) zookeeper.create("/current/RMB", "2.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/current/USD", false) == null) zookeeper.create("/current/USD", "12.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/current/JPY", false) == null) zookeeper.create("/current/JPY", "0.15".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/current/EUR", false) == null) zookeeper.create("/current/EUR", "9.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/totalAmount", false) == null) zookeeper.create("/totalAmount", "for total transaction amount".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/totalAmount/RMB", false) == null) zookeeper.create("/totalAmount/RMB", "0.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/totalAmount/USD", false) == null) zookeeper.create("/totalAmount/USD", "0.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/totalAmount/JPY", false) == null) zookeeper.create("/totalAmount/JPY", "0.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            if(zookeeper.exists("/totalAmount/EUR", false) == null) zookeeper.create("/totalAmount/EUR", "0.0".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    public static double getCurrentValue(String current) throws Exception {
        if(zookeeper == null) connectZookeeper();

        //System.err.println(zookeeper.getData("/current/" + current, false, null));
        String result = new String(zookeeper.getData("/current/" + current, false, null));
        return Double.parseDouble(result);
    }

    public static void setCurrentValue(String current, double value) throws Exception {
        if(zookeeper == null) connectZookeeper();
        String valueString = new Double(value).toString();
        zookeeper.setData("/current/" + current, valueString.getBytes(), -1);
    }

    public static double getTotalTxAmount(String current) throws Exception {
        if(zookeeper == null) connectZookeeper();
        String result = new String(zookeeper.getData("/totalAmount/" + current, false, null));
        return Double.parseDouble(result);
    }

    public static double updateTotalTxAmount(String current, double value) throws Exception {
        if(zookeeper == null) connectZookeeper();
        String curString = new String(zookeeper.getData("/totalAmount/" + current, false, null));
        Double curDouble = Double.parseDouble(curString);
        //System.out.println("get exchange " + current + "  price " + curString);
        curDouble += value;
        String valueString = curDouble.toString();
        //System.out.println("update exchange " + current + "  price " + valueString);
        zookeeper.setData("/totalAmount/" + current, valueString.getBytes(), -1);
        return curDouble;
    }

    public static void initTotalTxAmount(){
        try{
        if(zookeeper == null) connectZookeeper();
        zookeeper.setData("/totalAmount/RMB", "0.0".getBytes(), -1);
        zookeeper.setData("/totalAmount/USD", "0.0".getBytes(), -1);
        zookeeper.setData("/totalAmount/JPY", "0.0".getBytes(), -1);
        zookeeper.setData("/totalAmount/EUR", "0.0".getBytes(), -1);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
