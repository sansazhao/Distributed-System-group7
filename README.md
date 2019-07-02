# Distributed-System-group7
TODO：概述

> 背景：假设有一个热门的国际购物平台，它需要处理高并发的购物订单。因为它是为世界各地的用户设计，它应该能够支持不同的货币结算。当用户购买商品时，系统会根据当前汇率将原价格兑换成目标货币的价格。

实验目的：设计并实现一个分布式交易结算系统，接收和处理贸易订单，并记录所有交易结果和总交易金额。

实现工具：4 cloud machines，Zookeeper，Kafka， Spark，MySQL

## TODO
- [x]配置完成zookeeper + kafka + spark streaming
- [x]用spark streaming消费kafka的topic数据
- [x]使用zookeeper存储并写入汇率数据
- [x]使用mysql存储持久化数据
- []通过zookeeper实现访问商品信息前加锁
- []生成随机订单数据文件
- []通过http sender发送订单数据
- []在单机系统下完成订单处理
- []** 在分布式系统下完成订单处理 (完成基本任务) **
- []使用spark streaming的Direct API方式与kafka连接
- []采用分布式的文件系统(eg. hdfs)
- []采用分布式的Mysql(通过zookeeper管理)
- []使用不同的spark集群配置(eg. yarn mesos k8s)



## 1 System Environment
- centos
- 8GB DRAM
- 4-core CPU
- **TODO：集群分配的职能与示意图**

## 2 Install and Configuration
首先需要下载Zookeeper, Kafka, Spark等各种包，因此需要先安装wget指令

``` shell
yum -y install wget
```

### 2.1 安装Zookeeper

``` shell
wget
```
首先下载合适版本的包，这里选择了xxx

``` shell
tar zxvf xxx.tgz -C ~/soft
```
解压至指定文件夹

``` shell
cd ~/soft
ln -s zk xxx
```
创建软链接方便使用

随后修改默认的配置文件
``` shell
cd conf
cp zoo_sample.cfg zoo.cfg
```
zoo_sample.cfg为里面自带的样例配置文件，这里直接采用它，需要修改一下
``` shell

```


### 2.2 安装Kafka

``` shell
wget xxx
tar zxvf xxx.tgz -C ~/soft
cd ~/soft
ln -s kafka xxx
```


### 2.3 安装spark
spark与hadoop的关系
- spark使用hdfs作为分布式的文件系统，而在local或者standalone模式下不需要hdfs，因此不需要先安装hadoop

``` shell
wget xxx
tar zxvf xxx.tgz -C ~/soft
cd ~/soft
ln -s spark xxx
```


### 2.4 安装Hadoop(optional)


### 2.5 配置Mysql

选择使用第四台机器作为数据库服务器，本地配置mysql

``` shell
# 下载mysql的repo源
> wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm

# 安装mysql-community-release-el7-5.noarch.rpm包
> sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm

# 安装mysql，并按照命令行提示设置密码
> sudo apt-get install mysql-server

# 登录mysql
> mysql -u root -p

# 输入密码，假设密码为123456
> 123456

# 成功进入数据库，完成配置。
```

运行master.jar，可启动服务器。



2.6 docker?

```shell
> sudo apt-get install libcurl3-gnutls=7.47.0-1ubuntu2
> sudo apt-get install curl
```





## 3. Program Design

### 3.1 测试数据与testfile

### 3.2 Zookeeper事务管理

- 分布式锁的实现

- zookeeper存储汇率表，定义4个并行的threads对应4种货币，每分钟修改1次货币汇率。

### 3.3 Kafka缓存order flow

3.4 Spark Streaming计算

3.5 MySQL存储数据与结果

3.6 优化latency与throughput





## 4. Problems

** Q: kafka-console-consumer.sh --zookeeper xxx 报错 **

A: 因为版本更新该参数改为--bootstrap-server，需要broker server而不是zookeeper server

** Q: kafka-console-consumer.sh --zookeeper xxx 报错 **

A: 因为版本更新该参数改为--bootstrap-server，需要broker server而不是zookeeper server





## 5. Contribution

| 学号         | 姓名   | 分工 |
| ------------ | ------ | ---- |
| 516030910328 | 蔡忠玮 |      |
| 516030910219 | 徐家辉 |      |
| 516030910422 | 赵樱   |      |
| 516030910367 | 应邦豪 |      |
