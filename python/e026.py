#!/usr/bin/python
#python version:2.7.2

# A demo to show how to collect the suggestion words when we search with  search engine 

import urllib
import urllib2
import re
import time
from random import choice

wordsSearchingList = ['love', 'like', 'hate']

for item in wordsSearchingList:
	keyWord = urllib.quote(item)

	url = 'https://www.google.co.jp/complete/search?client=serp&hl=zh-CN&gs_rn=48&gs_ri=serp&tok=lwx3u9LNbTPjJBbBMlLYAw&pq=%s&cp=4&gs_id=8ea&q=%s&xhr=t' % (keyWord, keyWord)
	headers = {
		"GET": url,
		"Host": "www.google.co.jp",
		"Referer": "https://www.google.co.jp/",
		"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36",
	}

	req = urllib2.Request(url)

	for key in headers:
		req.add_header(key, headers[key])

	html = urllib2.urlopen(req).read()

	pattern = re.compile(r'"({searchWord}.*?)"'.format(searchWord = keyWord))
	results = pattern.findall(html)
	for item in results:
		print item
	time.sleep(1)
	print '-------------------------------------'