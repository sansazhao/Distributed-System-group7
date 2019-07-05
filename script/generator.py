#python3#
#coding:utf-8#
import json
import random
import time
NUM_OF_FILES = 3
NUM_OF_ORDERS = 10000
item_ids = [str(i) for i in range(1,500)]
total_map = {}

def random_generate_order():
    order = {"user_id": 123456,
        "initiator": "RMB",
        "time": 1558868400000,
        "items": [
            {"id": "1", "number": 2},
            {"id": "3", "number": 1}
        ]
    }
    order["user_id"] = random.randint(1,100000)
    order["initiator"] = random.choices(population = ["RMB","USD","JPY","EUR"],k=1)
    order["time"] = int(time.time())
    num = random.randint(1,len(item_ids) // 50)
    ids = random.sample(item_ids,num)
    order["items"].clear()
    for id in ids:
        num = random.randint(1,20)
        order["items"].append({
            "id":id, "number": num
        })
        if id in total_map:
            total_map[id] += num
        else:
            total_map[id] = num
    return order

def main():
    file_contents = [[] for i in range(NUM_OF_FILES)]
    for i in range(NUM_OF_ORDERS):
        file_contents[i % NUM_OF_FILES].append(random_generate_order())
    for index,file_content in enumerate(file_contents):
        with open('../data/input-{0}.json'.format(index), 'w') as f:
            json.dump(json.dumps(file_content), f)
    with open('../data/total-order-consume.json', 'w') as f:
        json.dump(json.dumps(total_map), f)


if __name__=="__main__":
    main()
