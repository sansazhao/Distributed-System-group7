#!/usr/bin/python3
import csv
import json
filename = "commodity-lock"
datas = []

lock_times = []
unlock_times = []
update_times = []
total_times = []
lock_portions = []
unlock_portions = []
update_portions = []

def avg(list):
    sum = 0.0
    for item in list:
        sum += item
    return sum / len(list)
with open(filename + ".csv",newline="") as f:
    reader= csv.reader(f)
    rows = [row for row in reader]
    for row in rows[1:]:
        lock_times.append(int(row[0]))
        unlock_times.append(int(row[1]))
        update_times.append(int(row[2]))
        total_times.append(int(row[3]))
        lock_portions.append(float(row[0]) / float(row[3]))
        unlock_portions.append(float(row[1]) / float(row[3]))
        update_portions.append(float(row[2]) / float(row[3]))
print("Implementation {0}:".format(filename))
print("平均加锁时间: {0}".format(avg(lock_times)))
print("平均解锁时间: {0}".format(avg(unlock_times)))
print("平均更新Tx时间: {0}".format(avg(update_times)))
print("平均任务时间: {0}".format(avg(total_times)))
print("平均加锁时间占比: {0}".format(avg(lock_portions)))
print("平均解锁时间占比: {0}".format(avg(unlock_portions)))
print("平均更新Tx时间占比: {0}".format(avg(update_portions)))
