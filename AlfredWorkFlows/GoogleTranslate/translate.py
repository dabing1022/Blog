# coding: utf-8

import requests
import url

CN_EN_URL = 'http://translate.google.cn/#zh-CN/en/'
EN_CN_URL = 'http://translate.google.cn/#en/zh-CN/'

def cn2en(cnCotent):
    r = requests.get('https://api.github.com/user', auth=('user', 'pass'))
    print(r.json())

if __name__ == '__main__':
    cn2en('test')
