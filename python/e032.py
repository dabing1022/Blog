# coding:utf-8

import urllib

def data2html(dataHrefStr):
	dataHrefStrLen = len(dataHrefStr)
	dataHrefInt = int(dataHrefStr)
	singleHtml = ''
	for i in range(0, dataHrefStrLen, 2):
		n = int(dataHrefStr[i:i+2]) + 23
		print n
		s = urllib.unquote('%' + hex(n)[2:]) # % + 0x6e ==> %6e
		print s
		singleHtml += s
	singleHtml = 'http://www.baidu.com' + singleHtml
	return singleHtml

singleHtml = data2html("2487242629253134333325263128343030312381938685")
print singleHtml