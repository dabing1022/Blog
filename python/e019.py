#!/usr/bin/python
#python version:2.7.2

#web crawler learning04
from urllib2 import Request, urlopen, URLError, HTTPError

req = Request('http://bbs.csdn.net/callmewhy')

try:

    response = urlopen(req)

except URLError, e:

    if hasattr(e, 'reason'):

        print 'We failed to reach a server.'

        print 'Reason: ', e.reason

    elif hasattr(e, 'code'):

        print 'The server couldn\'t fulfill the request.'

        print 'Error code: ', e.code

else:
    print 'No exception was raised.'
    # everything is fine
