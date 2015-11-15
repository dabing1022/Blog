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
import sys

# reload(sys)
# sys.setdefaultencoding('utf8')

DOMAIN = 'http://jiaoshi.zhikang.org'
savingDir = '/Volumes/Work/email/Yuwen/'
today = date.today().__str__()
today = today.replace('-', '')
savingDir += today
if not os.path.exists(savingDir):
	os.makedirs(savingDir)


def getDocUrlsByInput():
	docViewURLs = raw_input('请输入网址(多个网址用空格隔开）：')
	return docViewURLs.split(' ')

def getDocUrlsByReadingFile(filePath):
	return open(filePath, 'r').readlines()


studentDocDownloadPath = ''
teacherDocDownloadPath = ''
login_page = 'http://jiaoshi.zhikang.org/sixtteacher/login!newLogin.action?userName=1111&passWord=222'
headers = {
	'User-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36'
}
cookie = cookielib.CookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
req = urllib2.Request(url=login_page, headers=headers)
opener.open(req)

docViewURLs = getDocUrlsByReadingFile('url53.txt')
print docViewURLs

def downloadStudentAndTeacherDoc(viewURL, index):
	global savingDir, opener, studentDocDownloadPath, teacherDocDownloadPath

	req = urllib2.Request(url=viewURL, headers=headers)
	response = opener.open(req)
	html_content = response.read()
	soup = BeautifulSoup(html_content)

	studentDocDownloadPath = getStudentDocDownloadPath(soup)
	studentDocDownloadPath = DOMAIN + studentDocDownloadPath
	studentDocDownloadPath = studentDocDownloadPath.encode('utf-8')
	STUDENT_FILE_NAME = getFileSavingName()
	studentPattern = re.compile('(.+/doc/)(.+)\.\w+')
	studentRe = studentPattern.match(studentDocDownloadPath)
	studentDocName = studentRe.group(2)
	studentDocSavingPath = savingDir + '/' + STUDENT_FILE_NAME
	print '\n'
	print '''第 %d 份学生文档信息------------------------下载开始''' % (index)
	print 'Student file name ----------> ' + STUDENT_FILE_NAME
	print 'Student file saving path ----------> ' + studentDocSavingPath
	print u'文档保存中，请稍候...'
	urllib.urlretrieve(studentDocDownloadPath, studentDocSavingPath)
	print u'第 %d 份学生文档保存完毕' % (index)

	teacherSwfFilePath = getTeacherSwfFilePath(html_content)
	TEACHER_FILE_NAME = getFileSavingName()
	file_fileTypeList = TEACHER_FILE_NAME.split('.')
	teacherFileSuffix = '_teacher'
	TEACHER_FILE_NAME = file_fileTypeList[0] + teacherFileSuffix + "." + file_fileTypeList[-1]

	teacherPattern = re.compile('(.+/swf/)(.+)\.\w+')
	teacherRe = teacherPattern.match(teacherSwfFilePath)
	teacherDocName = teacherRe.group(2)

	teacherDocDownloadPath = studentDocDownloadPath.replace(studentDocName, teacherDocName)
	teacherDocSavingPath = savingDir + '/' + TEACHER_FILE_NAME

	print '\n'
	print '''第 %d 份教师文档信息------------------------下载开始''' % (index)
	print 'Teacher file name ----------> ' + TEACHER_FILE_NAME
	print 'Teacher file saving path ----------> ' + teacherDocSavingPath
	print u'文档保存中，请稍候...'
	urllib.urlretrieve(teacherDocDownloadPath, teacherDocSavingPath)
	print u'第 %d 份教师文档保存完毕' % (index)

def getFileSavingName():
	global studentDocDownloadPath
	filename = studentDocDownloadPath.split('=')[-1]
	extentsion = re.match(r'.+(\.\w+)&.+', studentDocDownloadPath).group(1)
	filename += extentsion
	return filename

def getStudentDocDownloadPath(soup):
	return soup.find_all('a')[-3].get('href')

def getTeacherSwfFilePath(html_content):
	swfLinkPattern = re.compile('http://.+\.swf')
	swfsLinks = swfLinkPattern.findall(html_content)
	teacherSwfFilePath = swfsLinks[-1]
	return teacherSwfFilePath

index = 1
for viewURL in docViewURLs:
	downloadStudentAndTeacherDoc(viewURL, index)
	index += 1
