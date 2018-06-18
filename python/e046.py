#!/usr/local/bin/python3

from functools import reduce

def multi2(num):
    return num * 2

if __name__ == "__main__":
    result = []
    arr = [1, 2, 3]
    test = list(map(multi2, arr))
    print(test)

    test2 = list(map(multi2Arr, arr))
    print(test2)

    items = [1, 2, 3, 4, 5]
    squared = list(map(lambda x: x**2, items))
    print(squared)
