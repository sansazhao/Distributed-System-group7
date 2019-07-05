package Core;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZooKeeperPool {
    static ZooKeeper zookeeper;
    static String zookeeper_server;

    static public void setZooKeeperServer(String server){
        zookeeper_server = server;
    }

    static public ZooKeeper getZookeeper() {
        if(zookeeper == null){
            try {
                zookeeper = new ZooKeeper(zookeeper_server, 2000, null);
            }catch(IOException e){
                e.printStackTrace();

            }
        }
        return zookeeper;
    }
}
