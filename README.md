# Distributed-System-group7
an order system cluster  deployed with zookeeper&amp;kafka&amp;spark&amp;mysql on 4 cloud machines

## 1 System Environment
- centos
- 8GB DRAM
- 4-core CPU

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

## Q & A
** Q: kafka-console-consumer.sh --zookeeper xxx 报错 **

A: 因为版本更新该参数改为--bootstrap-server，需要broker server而不是zookeeper server

** Q: kafka-console-consumer.sh --zookeeper xxx 报错 **

A: 因为版本更新该参数改为--bootstrap-server，需要broker server而不是zookeeper server
