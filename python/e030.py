#coding: utf-8


import dbm

print '初步了解下数据库相关知识'

db = dbm.open('websites', 'c')

#add an item
db['www.python.org'] = 'Python home page'

print(db['www.python.org'])

#close and save to disk
db.close