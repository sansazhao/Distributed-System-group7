import requests
import json
import time
import random

SERVER_ADDRESS="http://202.120.40.8:30441"

def init():
    currencies = ["RMB","USD",'JPY',"EUR"]
    with open("../data/total-order-consume.json") as f:
        total_map = json.load(f)
        total_map = json.loads(total_map)
    #print(total_map)
    for key in total_map:
        print(key)
        print("id {0} commodity consume {1} floor to {2}".format(key,total_map[key],((total_map[key] // 100) + 1) * 100 ))
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
    init()




if __name__=="__main__":
    main()
