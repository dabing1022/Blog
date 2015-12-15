# coding: utf-8

# 1. 首先我们导入了 Flask 类。这个类的实例将会成为我们的 WSGI 应用
from flask import Flask

# 2. 接着我们创建了这个类的实例。第一个参数是应用模块或者包的名称。如果你使用一个 单一模块（就像本例），那么应当使用 __name__ ，因为名称会根据这个模块是按 应用方式使用还是作为一个模块导入而发生变化（可能是 '__main__' ，也可能是 实际导入的名称）。这个参数是必需的，这样 Flask 就可以知道在哪里找到模板和 静态文件等东西。更多内容详见 Flask 文档。
app = Flask(__name__)
app.debug = True

#############################################################################
# 3.然后我们使用 route() 装饰器来告诉 Flask 触发函数的 URL 。
@app.route('/')
# 4. 函数名称可用于生成相关联的 URL ，并返回需要在用户浏览器中显示的信息。
def index():
    return 'Index Page'

@app.route('/hello')
def hello():
    return 'Hello World'

@app.route('/user/<username>')
def show_user_profile(username):
    return 'User %s' % username

# <converter:variable_name>
# converter:
# 1. int 2. float 3. path
@app.route('/post/<int:post_id>')
def show_post(post_id):
    return 'Post %d' % post_id

@app.route('/projects/')
def projects():
    return 'The project page'

@app.route('/about')
def about():
    return 'The about page'
#############################################################################


# 5.最后，使用 run() 函数来运行本地服务器和我们的应用。 if __name__ == '__main__': 确保服务器只会在使用 Python 解释器运行代码的情况下运行，而不会在作为模块导入时运行。
if __name__ == '__main__':
    app.run()
