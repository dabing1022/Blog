# coding:utf-8

# https://github.com/tqdm/tqdm

from time import sleep
from tqdm import tqdm
from requests import Request, Session
from faker import Factory

BASE_URL = ""
REGISTER_URL = BASE_URL + ''
LOGIN_URL = BASE_URL + ''

fake = Factory.create('zh_CN')

def register(session):
    phone = fake.phone_number()
    print(phone)
    registerData = {
        "mobile": phone,
        "password": 'qieiepwpw'
    }
    registerReq = Request('POST', REGISTER_URL, data = registerData)
    registerPrepped = registerReq.prepare()
    registerResp = session.send(registerPrepped)
    print(registerResp.status_code)
    print(registerResp.headers)
    print(registerResp.content)

def login(session):
    loginData = {
        "mobile": 18512900013,
        "password": 'qieiepwpw'
    }

    loginReq = Request('POST', LOGIN_URL, data = loginData)
    loginPrepped = loginReq.prepare()
    loginResp = session.send(loginPrepped)
    print(loginResp.headers)
    print(loginResp.content)

if __name__ == '__main__':
    session = Session()
    # for i in tqdm(range(1000)):
    #     sleep(0.01)
    register(session)
    login(session)
