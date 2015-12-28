# coding:utf-8

import smtplib
import os
from email.mime.text import MIMEText
from email.header import Header
from email.utils import parseaddr, formataddr
from apscheduler.schedulers.blocking import BlockingScheduler

def _format_addr(s):
    name, addr = parseaddr(s)
    return formataddr((\
        Header(name, 'utf-8').encode(), \
        addr.encode('utf-8') if isinstance(addr, unicode) else addr))

from_addr = 'zhaoweibing@didapinche.com'
password = 'hiphop5566'
# smtp_server = 'smtp.126.com'
# to_addr = 'xxx@126.com'
smtp_server = 'smtp.exmail.qq.com'
to_addr = ['zhaoweibing@didapinche.com', 'wuxiaotao@didapinche.com', 'tangzhifei@didapinche.com', 'lichenghang@didapinche.com', 'shimengjun@didapinche.com', 'wangcaihui@didapinche.com']

msg = MIMEText('<html><body><h1>这是标题</h1>' +
    '<p>这不是我手动写的，这是程序发送的！哈哈哈</p>' +
    '</body></html>', 'html', 'utf-8')
msg['From'] = _format_addr(u'大兵 <%s>' % from_addr)
msg['To'] = _format_addr(u'小兄弟/小妹妹 <%s>' % to_addr)
msg['Subject'] = Header(u'神秘邮件', 'utf-8').encode()
server = smtplib.SMTP(smtp_server, 25) # SMTP协议默认端口是25
server.set_debuglevel(1)
server.login(from_addr, password)
# server.quit()
def send_email():
    server.sendmail(from_addr, to_addr, msg.as_string())

if __name__ == '__main__':
    scheduler = BlockingScheduler()
    job = scheduler.add_job(send_email, 'interval', seconds=3)
    scheduler.start()
