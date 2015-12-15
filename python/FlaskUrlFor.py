# coding:utf-8

from flask import Flask, url_for

app = Flask(__name__)

@app.route('/')
def index(): pass

@app.route('/login')
def login(): pass

@app.route('/user/<username>')
def profile(username): pass

# url_for() 函数就是用于构建指定函数的 URL
# 1. 反向解析通常比硬编码 URL 更直观。同时，更重要的是你可以只在一个地方改变 URL ，而不用到处乱找。
# 2. URL创建会为你处理特殊字符的转义和 Unicode 数据，不用你操心。
# 3. 如果你的应用是放在 URL 根路径之外的地方（如在 /myapplication 中，不在 / 中）， url_for() 会为你妥善处理。
with app.test_request_context():
    print url_for('index')
    print url_for('login')
    print url_for('login', next = '/')
    print url_for('profile', username = "andy")

if __name__ == '__main__':
    app.run()
