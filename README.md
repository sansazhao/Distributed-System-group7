# Distributed-System-group7
TODO：概述

> 背景：假设有一个热门的国际购物平台，它需要处理高并发的购物订单。因为它是为世界各地的用户设计，它应该能够支持不同的货币结算。当用户购买商品时，系统会根据当前汇率将原价格兑换成目标货币的价格。

实验目的：设计并实现一个分布式交易结算系统，接收和处理贸易订单，并记录所有交易结果和总交易金额。

实现工具：4 cloud machines，Zookeeper，Kafka， Spark，MySQL


## TODO
-  ~~配置完成zookeeper + kafka + spark streaming~~
-  ~~用spark streaming消费kafka的topic数据~~
-  ~~使用zookeeper存储并写入汇率数据~~
-  ~~使用mysql存储持久化数据~~
-  ~~通过zookeeper实现访问商品信息前加锁~~
-  ~~生成随机订单数据文件~~
-  ~~在应用开始时重置数据库~~
-  通过http sender发送订单数据
-  ~~通过http receiver接受数据并发送给kafka~~
-  通过zookeeper实现total transaction num的查询
-  ~~启动并行单元实时更改汇率数据~~
-  ~~在单机系统下完成订单处理~~
-  **~~在分布式系统下完成订单处理 (完成基本任务)~~**
-  使用spark streaming的Direct API方式与kafka连接
-  采用分布式的文件系统(eg. hdfs)
-  采用分布式的Mysql(通过zookeeper管理)
-  使用不同的spark集群配置(eg. yarn mesos k8s)




## 1 System Environment
### 1.1 云服务器配置

- centos
- 8GB DRAM
- 4-core CPU

### 1.2 集群预览

- **TODO：集群分配的职能与示意图**

  

## 2 Install and Configuration
首先需要下载Zookeeper, Kafka, Spark等各种包，因此需要先安装wget指令

``` shell
yum -y install wget
```

配置四台虚拟机之间的域名映射

``` shell
# /etc/hosts
10.0.0.43   dist-1
10.0.0.18   dist-2
10.0.0.86   dist-3
10.0.0.47   dist-4
```

### 2.1 安装Zookeeper

``` shell
wget https://mirrors.tuna.tsinghua.edu.cnlog4j/apache/zookeeper/zookeeper-3.4.14/zookeeper-3.4.14.tar.gz
```
首先下载合适版本的包，这里选择了zookeeper-3.4.14.tar.gz

``` shell
tar zxvf zookeeper-3.4.14.tar.gz -C ~/soft
```
解压至指定文件夹

``` shell
cd ~/soft
ln -s zookeeper-3.4.14 zk
```
创建软链接方便使用

随后修改默认的配置文件
``` shell
cd conf
cp zoo_sample.cfg zoo.cfg
```
zoo_sample.cfg为里面自带的样例配置文件，这里直接采用它，需要修改一下
``` shell
# ~/soft/zk/conf/zoo.cfg
dataDir=/home/centos/zookeeper/data
server.1=dist-1:2888:3888
server.2=dist-2:2888:3888
server.3=dist-3:2888:3888
```


### 2.2 安装Kafka

``` shell
wget http://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.2.1/kafka_2.11-2.2.1.tgz
tar zxvf kafka_2.11-2.2.1.tgz -C ~/soft
cd ~/soft
ln -s kafka_2.11-2.2.1.tgz kafka
```
修改配置文件

``` shell
# ~/soft/kafka/config/server.properties
broker.id=0 # different in each node

zookeeper.connect=dist-1:2181,dist-2:2181,dist-3:2181  #zookeeper config

```



### 2.3 安装spark
spark与hadoop的关系
- spark使用hdfs作为分布式的文件系统，而在local或者standalone模式下不需要hdfs，因此不需要先安装hadoop


``` shell
wget http://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.3/spark-2.4.3-bin-hadoop2.7.tgz
tar zxvf spark-2.4.3-bin-hadoop2.7.tgz -C ~/soft
cd ~/soft
ln -s spark-2.4.3-bin-hadoop2.7.tgz spark
```

修改配置文件
``` shell
# ~/soft/spark/conf/slaves
dist-1
dist-2
dist-3
```
在三台机器上都配置slaves文件



### 2.4 安装Hadoop(optional)

### 2.5 配置Mysql

选择使用第四台机器作为数据库服务器，本地配置mysql

``` shell
# 下载mysql的repo源
> wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

# 安装mysql-community-release-el7-5.noarch.rpm包
> sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm

# 安装mysql，并按照命令行提示设置密码
> sudo yum install mysql-server -y

# 登录mysql
> mysql -u root -p

# 创建数据库lab5和表格commodity, result
create database lab5;
create table commodity(
  id        INT         NOT NULL PRIMARY KEY,
  name      VARCHAR(11) NULL,
  price     DOUBLE      NULL,
  currency  VARCHAR(5)  NULL,
  inventory INT         NULL) ENGINE = InnoDB;

create table result(
  id        INT AUTO_INCREMENT PRIMARY KEY,
  user_id   INT        NULL,
  initiator VARCHAR(5) NULL,
  success   VARCHAR(5) NULL,
  paid      DOUBLE     NULL) ENGINE = InnoDB;

```

运行master.jar，可启动服务器。







## 3. Program Design

### 3.1 测试数据与testfile

### 3.2 Zookeeper事务管理

- 分布式锁的实现

- zookeeper存储汇率表，定义4个并行的threads对应4种货币，每分钟修改1次货币汇率。

### 3.3 Kafka缓存order flow

![](C:\Users\sansazhao\Desktop\my_work\Distributed-System-group7\picture\zk&kafka.png)

### 3.4 Spark Streaming计算

### 3.5 MySQL存储数据与结果

![](C:\Users\sansazhao\Desktop\my_work\Distributed-System-group7\picture\spark&zk.png)

### 3.6 优化latency与throughput





## 4. Problems

**Q1: kafka-console-consumer.sh --zookeeper xxx 报错**

A: 因为版本更新该参数改为--bootstrap-server，需要broker server而不是zookeeper server



**Q2: zkServer.sh start后status显示not running**

A: 可查看zookeeper.out文件
```
org.apache.zookeeper.server.quorum.QuorumPeerConfig$ConfigException: Error processing /home/centos/soft/zk/bin/../conf/zoo.cfg
        at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parse(QuorumPeerConfig.java:156)
        at org.apache.zookeeper.server.quorum.QuorumPeerMain.initializeAndRun(QuorumPeerMain.java:104)
        at org.apache.zookeeper.server.quorum.QuorumPeerMain.main(QuorumPeerMain.java:81)
Caused by: java.lang.IllegalArgumentException: /home/centos/zookeeper/data/myid file is missing
        at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parseProperties(QuorumPeerConfig.java:408)
        at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parse(QuorumPeerConfig.java:152)
        ... 2 more

```
- 由于dataDir下的myid文件未创建
- 若日志显示正常却status未显示，可能由于集群模式还未完成选举，等所有机器都启动后再查看



**Q3: Field "id" doesn't have a default value**

A: 由于使用hibernete将Result表的id列设置为```@GeneratedValue(strategy = GenerationType.IDENTITY)```因此自增属性交由Mysql管理，而生产环境下的Mysql未配置id为AUTO INCREMENT，因此报错，通过```alter table Result modify id int AUTO INCREMENT;```修改完毕，需要保证连接数据库的进程关闭，否则会卡死。



**Q4：产生死锁**

A：

## 5. Contribution

| 学号         | 姓名   | 分工 |
| ------------ | ------ | ---- |
| 516030910328 | 蔡忠玮 |      |
| 516030910219 | 徐家辉 |      |
| 516030910422 | 赵樱   |      |
| 516030910367 | 应邦豪 |      |

**项目Github**：https://github.com/sansazhao/Distributed-System-group7