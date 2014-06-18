#!/usr/bin/python
#python version:2.7.2

#web crawler learning01
import urllib2
# http ftp file
response = urllib2.urlopen('http://www.baidu.com/')
# req = urllib2.Request('http://www.baidu.com/')
# response = urllib2.urlopen(req)
html = response.read()
print html
