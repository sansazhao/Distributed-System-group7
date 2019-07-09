#!/usr/bin/python3#
#coding:utf-8
import sys
import json
import requests
import random
import time
import _thread
import threading
import os
from queue import Queue
SERVER_ADDRESS="http://dist-1:30441"
dataDir = "./"
NUM_OF_THREADS = 0
order_queue = Queue()


def thread(threadName,interval):
    #print("thread")
    while not order_queue.empty():
        order = order_queue.get()
        #print(order)
        response = send_order(order)
        print("Thread {0} {1} order send result: {2}".format(threadName,order["count"],response.text))
        time.sleep(interval)


def send(threadName,fileName,interval,num_of_threads):
    count = 0
    #print("start")
    with open(fileName,"r") as f:
        data = json.loads(json.load(f))
        for order in data:
            order["initiator"] = order["initiator"][0]

            order["thread_name"] = threadName
            order["count"] = count
            order_queue.put(order)
            #response = send_order(order)
            count += 1
            #print("Thread {0} {1} order send result: {2}".format(threadName,count,response.text))
            #time.sleep(interval)
    threads = []
    for i in range(num_of_threads):
        threads.append(threading.Thread(target=thread,args = (threadName + "-" + str(i),interval)))
    for t in threads:
        t.start()
    for t in threads:
        t.join()




def main():
    files = ["input-0.json","input-1.json","input-2.json"]
    global SERVER_ADDRESS
    global dataDir
    NUM_OF_THREADS = int(sys.argv[1])
    #order_queue = Queue(NUM_OF_THREADS)
    #print(sys.argv)
    if len(sys.argv) >= 6:
        SERVER_ADDRESS = sys.argv[5]
    if len(sys.argv) >= 5:
        dataDir = sys.argv[4]
        #print(dataDir)

    id = int(sys.argv[2])
    interval = float(sys.argv[3])
        #t = threading.Thread(target=send,args=("Thread-" + str(id),os.path.join(dataDir,files[id]),interval,NUM_OF_THREADS))
    send("Thread-" + str(id),os.path.join(dataDir,files[id]),interval,NUM_OF_THREADS)
        #t.start()

    #t.join()


def send_order(body):
    return requests.post(SERVER_ADDRESS + "/post/order", data = json.dumps(body))



if __name__=="__main__":
    main()
