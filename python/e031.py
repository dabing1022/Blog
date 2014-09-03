#coding:utf-8

from urlparse import urlparse
import urllib

'''
scheme 网络协议或者下载规划
netloc 服务器位置
path 斜杠限定文件或者CGI应用程序的路径
params 可选参数
query 连接符&连接键值对
frag 拆分文档中的特殊锚
'''



print '-----------------------------------------------------'
result = urlparse('http://www.cwi.nl:80/%7Eguido/Python.html')

print result
print result.scheme
print result.netloc
print result.path
print result.port
print result.geturl()

print '-----------------------------------------------------'
result = urlparse('//www.cwi.nl:80/%7Eguido/Python.html')
print result

print '-----------------------------------------------------'
result = urlparse('www.cwi.nl/%7Eguido/Python.html')
print result

print '-----------------------------------------------------'
result = urlparse('help/Python.html')
print result

print '-----------------------------------------------------'
urllib.urlretrieve("http://www.baidu.com", "res/baidu.html")