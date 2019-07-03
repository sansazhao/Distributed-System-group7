FROM openjdk:8
VOLUME /tmp
ADD ./target/app-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
RUN yum -y install wget
RUN wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
RUN rpm -ivh /root/mysql-community-release-el7-5.noarch.rpm
RUN yum install mysql-server -y
RUN mysql_install_db --user=mysql
ENV MYSQL_USER root
ENV MYSQL_PASS =
RUN /etc/init.d/mysqld start &&\  
    mysql -e "grant all privileges on *.* to 'root'@'%' identified by '';"&&\  
    mysql -e "grant all privileges on *.* to 'root'@'localhost' identified by '';"&&\  
    mysql -e "create database lab5;use lab5;create table commodity(id INT NOT NULL PRIMARY KEY, name VARCHAR(11) NULL,price DOUBLE NULL,currency  VARCHAR(5)  NULL,inventory INT NULL) ENGINE = InnoDB;"&&\
    mysql -e "use lab5;create table result(id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NULL,initiator VARCHAR(5) NULL,success VARCHAR(5) NULL,paid DOUBLE NULL) ENGINE = InnoDB;"&&\
    mysql -e "use lab5;show tables;"
   
EXPOSE 3306  
   
CMD ["/usr/bin/mysqld_safe"]  
CMD ["java","-jar","/app.jar"]
