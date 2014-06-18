#!/usr/bin/python
#python version:2.7.2

#web crawler learning03
from urllib2 import Request, urlopen, URLError, HTTPError

req = Request('http://bbs.csdn.net/callmewhy')

try:
    response = urlopen(req)
except HTTPError, e:
    print 'The server couldn\'t fullfill the request.'
    print 'Error code:', e.code

except URLError, e:
    print 'We failed to reach a server.'
    print 'Reason:', e.reason
else:
    print 'No exception was raised.'
