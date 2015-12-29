# coding:utf-8

from flask import Flask, render_template
from flask.ext.bootstrap import Bootstrap

app = Flask(__name__)
app.debug = True
bootstrap = Bootstrap(app)

@app.route('/')
@app.route('/index')
def index():
    return render_template('FlaskBootstrap_test01.html')

if __name__ == '__main__':
    app.run()
