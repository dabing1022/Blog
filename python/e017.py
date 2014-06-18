#!/usr/bin/python
#python version:2.7.2

#web crawler learning02
import urllib2
req = urllib2.Request('http://bbs.csdn.net/callmewhy')
try: urllib2.urlopen(req)

except urllib2.URLError, e:
    print "Reason:", e.reason
# HTTPError code
    print "Code:", e.code
