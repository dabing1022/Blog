#!/usr/bin/python
#coding:utf-8
# BeautifulSoup

from bs4 import BeautifulSoup
from datetime import datetime, date
import cookielib
import os
import os.path
import urllib
import urllib2
import re

DOMAIN = 'http://jiaoshi.zhikang.org'
savingDir = '/Volumes/Work/email/Yuwen/'
today = date.today().__str__()
today = today.replace('-', '')
savingDir += today
if not os.path.exists(savingDir):
	os.makedirs(savingDir)
docViewURLs = raw_input('请输入网址(多个网址用空格隔开）：')
docViewURL_list = docViewURLs.split(' ')
studentDocDownloadPath = ''
teacherDocDownloadPath = ''
login_page = 'http://jiaoshi.zhikang.org/sixtteacher/login!login.action?teaLoginArgsDto.userName=%E5%88%81%E9%9B%AA%E8%90%8D&teaLoginArgsDto.password=198720dd&teaLoginArgsDto.cityCode=020&modeType=0&t=0.9819506246130913'
headers = {
	'User-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36'
}

cookie = cookielib.CookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
req = urllib2.Request(url=login_page, headers=headers)
opener.open(req)


def downloadTeacherDoc(viewURL, index):
	global savingDir, opener, studentDocDownloadPath, teacherDocDownloadPath

	req = urllib2.Request(url=viewURL, headers=headers)
	response = opener.open(req)
	html_content = response.read()
	soup = BeautifulSoup(html_content)

	studentDocDownloadPath = getStudentDocDownloadPath(soup)
	teacherSwfFilePath = getTeacherSwfFilePath(html_content)
	FILE_NAME = getFileSavingName()

	studentPattern = re.compile('(.+/doc/)(.+)\.\w+')
	studentRe = studentPattern.match(studentDocDownloadPath)
	studentDocName = studentRe.group(2)

	teacherPattern = re.compile('(.+/swf/)(.+)\.\w+')
	teacherRe = teacherPattern.match(teacherSwfFilePath)
	teacherDocName = teacherRe.group(2)

	teacherDocDownloadPath = studentDocDownloadPath.replace(studentDocName, teacherDocName)
	teacherDocDownloadPath = DOMAIN + teacherDocDownloadPath
	teacherDocDownloadPath = teacherDocDownloadPath.encode('utf-8')
	savingPath = savingDir + '/' + FILE_NAME

	print '\n'
	print '''第 %d 份教师文档信息------------------------''' % (index)
	print u'文件名称----------> ' + FILE_NAME
	print u'保存地址----------> ' + savingPath
	print u'文档保存中，请稍候...'
	urllib.urlretrieve(teacherDocDownloadPath, savingPath)


def getFileSavingName():
	global studentDocDownloadPath
	filename = studentDocDownloadPath.split('=')[-1]
	extentsion = re.match(r'.+(\.\w+)&.+', studentDocDownloadPath).group(1)
	filename += extentsion
	return filename

def getStudentDocDownloadPath(soup):
	return soup.find_all('a')[-1].get('href')

def getTeacherSwfFilePath(html_content):
	swfLinkPattern = re.compile('http://.+\.swf')
	swfsLinks = swfLinkPattern.findall(html_content)
	teacherSwfFilePath = swfsLinks[-1]
	return teacherSwfFilePath

index = 1
for viewURL in docViewURL_list:
	downloadTeacherDoc(viewURL, index)
	index += 1