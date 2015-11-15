# coding:utf-8

from wkhtmltopdf import wkhtmltopdf
# wkhtmltopdf(url='http://www.baidu.com', output_file='/Volumes/Work/email/Yuwen/out.pdf')

import pdfkit
config = pdfkit.configuration(wkhtmltopdf='/Library/Python/2.7/site-packages/wkhtmltopdf/')
pdfkit.from_string('http://stackoverflow.com', '/Volumes/Work/email/Yuwen/out.pdf', configuration=config)
