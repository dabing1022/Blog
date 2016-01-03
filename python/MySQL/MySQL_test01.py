# coding:utf-8

# FlaskWebDevelop：Chapter5
import os
from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy

basedir = os.path.abspath(os.path.dirname(__file__))
app = Flask(__name__)
app.debug = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'data.sqlite')
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
db = SQLAlchemy(app)

class Role(db.Model):
    __tablename__ = 'roles'
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(64), unique = True)
    users = db.relationship('User', backref = 'role')

    def __repr__(self):
        return '<Role %r>' % self.name

class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key = True)
    username = db.Column(db.String(64), unique = True, index = True)
    role_id = db.Column(db.Integer, db.ForeignKey('roles.id'))

    def __repr__(self):
        return '<User %r>' % self.username

db.drop_all()
db.create_all()

admin_role = Role(name='Admin')
mod_role = Role(name='Moderator')
user_role = Role(name='User')
user_john = User(username='john', role=admin_role)
user_susan = User(username='susan', role=user_role)
user_david = User(username='david', role=user_role)

print(admin_role.id)
print(mod_role.id)
print(user_role.id)

# 数据库会话（事务）
db.session.add(admin_role)
db.session.add(mod_role)
db.session.add(user_role)
db.session.add(user_john)
db.session.add(user_susan)
db.session.add(user_david)
# db.session.add_all([admin_role, mod_role, user_role, user_john, user_susan, user_david])
db.session.commit()

print(admin_role.id)
print(mod_role.id)
print(user_role.id)

##### modify
print('before modify:' + admin_role.name)
admin_role.name = 'Administrator'
db.session.add(admin_role)
db.session.commit()
print('after modify:' + admin_role.name)

#### delete
db.session.delete(mod_role)
db.session.commit()

#### Query
print(Role.query.all())
print(User.query.all())
print(User.query.filter_by(role=user_role).all())
print(str(User.query.filter_by(role=user_role)))
