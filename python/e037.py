# coding: utf-8
import time

def task():
    print "task ..."

def timer(n):
    while True:
        print time.strftime('%Y-%m-%d %X',time.localtime())
        task()
        time.sleep(n)

if __name__ == '__main__':
    timer(5)
