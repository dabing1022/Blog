#!/usr/bin/python
#coding:utf-8
# BeautifulSoup

from bs4 import BeautifulSoup
from datetime import datetime, date
import cookielib
import os
import requests
import os.path
import urllib
import urllib2
import re

username = 'username'
password = 'password'

session = requests.Session()
headers = {
	'User-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36'
}
rsp = session.get('https://github.com/login')
page_login = BeautifulSoup(rsp.text, 'html.parser')
post_form = page_login.find('div', id='login')
payload = {
    ip['name']: ip['value']
    for ip in post_form.findAll('input')
    if ip.get('value', None)
}
print(payload)
payload['login'] = username
payload['password'] = password

rsp = session.post('https://github.com/session', data=payload, headers=headers)
print('login:' + str(rsp.status_code))
if session.cookies['logged_in'] != 'yes':
    print 'login failed.'
else:
    print 'login success.'

rsp = session.post('https://github.com/users/follow?target=test')
print('follow:' + str(rsp.status_code))
