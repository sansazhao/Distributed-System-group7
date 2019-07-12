import requests
import json
import time
import sys
import random
import threading
from queue import Queue

SERVER_ADDRESS="http://202.120.40.8:30441"
currencies = ["RMB","USD",'JPY',"EUR"]
global_map = {}
queue = Queue()
def init(num):
    global total_map
    with open("../data/total-order-consume.json") as f:
        total_map = json.load(f)
        total_map = json.loads(total_map)
    #print(total_map)
    for key in total_map:
        queue.put(key)
    threads = []
    for i in range(num):
        threads.append(threading.Thread(target=send,args = ()))
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join()

def send():
    while not queue.empty():
        key = queue.get()
        commodity = {
            "id":int(key),
            "inventory":((total_map[key] // 100) + 1) * 100 ,
            "price":random.randint(1,20) * 1.0,
            "name":'test' + key,
            "currency":random.choice(currencies)
        }
        response = insert_commodity(commodity)
        print(key + "  " + response.text)

def insert_commodity(body):
    return requests.post(SERVER_ADDRESS + "/insert/commodity", data = json.dumps(body))

def clear_database():
    return requests.get(SERVER_ADDRESS + "/clear/database")

def main():
    print(clear_database().text)
    num_of_threads = int(sys.argv[1])
    init(num_of_threads)




if __name__=="__main__":
    main()
