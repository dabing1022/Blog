#!/usr/bin/python
#python version:2.7.2

#web crawler learning05
from urllib2 import Request, urlopen, URLError, HTTPError
# geturl()
# info()
old_url = 'http://rrurl.cn/b1UZuP'
req = Request(old_url)
response = urlopen(req)
print 'Old url: ' + old_url
print 'Real url: ' + response.geturl()
print 'Info: ', response.info()
