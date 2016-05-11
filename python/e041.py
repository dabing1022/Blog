# coding:utf-8

# https://github.com/tqdm/tqdm

from time import sleep
from tqdm import tqdm

if __name__ == '__main__':
    for i in tqdm(range(1000)):
        sleep(0.01)
