#coding:utf-8

"""A project to make lots of stars.
GitHub does not remove stars when accounts are deleted.
1. Accounts = [new_account() || lists:seq(1, 9001)]
2. [{star_repo(?REPO, A), delete_account(A)} || A <- Accounts]
3. ???
4. free stars
The GitHub database isn't properly updated using this sequence of operations.
Hopefully GitHub can fix this silly bug ))
"""

import requests
import re
import time
from sys import argv

HOST = "github.com"
MAIN_URL = "https://github.com"
LOGIN_URL = "https://github.com/login"
SESSION_URL = "https://github.com/session"
ADMIN_URL = "https://github.com/settings/admin"
JOIN_URL = "https://github.com/join"
DELETE_URL = "https://github.com/users/{username}"
REPO_URL = "https://github.com/{repo}/"
USER_URL = "https://github.com/{user}"
FOLLOW_URL = "https://github.com/users/follow?target={user}"


def _get_auth_token(html, multi_page=False):
    """Tony the pony would not approve."""
    if multi_page:
        return re.findall("action=\".*/(\\w+).*?"
                          "<input name=\"authenticity_token\" type=\"hidden\" "
                          "value=\"(.*?)\" />", html)
    else:
        return re.findall("<input name=\"authenticity_token\" type=\"hidden\" "
                          "value=\"(.*?)\" />", html)[0]

def _get_all_users(html):
    return re.findall('<img alt="@(\w+)" class="avatar" height="48"', html)


def create_account(username, password, email):
    """Create a GitHub account."""
    session = requests.session()
    page = session.get(JOIN_URL).content.decode()
    authenticity_token = _get_auth_token(page)
    data = {'utf8': "✓",
            'authenticity_token': authenticity_token,
            'user[login]': username,
            'user[email]': email,
            'user[password]': password,
            'user[password_confirmation]': password,
            'source_label': "Detail+Form"}
    headers = {'Host': HOST,
               'Referer': JOIN_URL}
    return session.post(JOIN_URL, headers=headers, data=data)


def login(username, password, session):
    """Log in the given requests session object."""
    page = session.get(LOGIN_URL).content.decode()
    authenticity_token = _get_auth_token(page)

    data = {'utf8': "✓",
            'authenticity_token': authenticity_token,
            'login': username,
            'password': password,
            'commit': "Sign+in"}
    headers = {'Host': HOST,
               'Referer': MAIN_URL}
    return session.post(SESSION_URL, headers=headers, data=data)


def delete_account(username, password, manual=True):
    """Be careful when setting manual to False!"""
    session = requests.session()
    login(username, password, session)

    page = session.get(ADMIN_URL).content.decode()
    authenticity_token = _get_auth_token(page)
    data = {'utf8': "✓",
            '_method': "delete",
            'authenticity_token': authenticity_token,
            'sudo_login': username,
            'confirmation_phrase': "delete my account"}
    headers = {'Host': HOST,
               'Referer': MAIN_URL}
    if manual and input("Really delete {} ? [y/N] ".format(username)) != 'y':
        print("Account not deleted.")
        return False
    return session.post(DELETE_URL.format(username=username),
                        headers=headers, data=data)


def star_repo(repo, session):
    """Star a github repo."""
    repo_url = REPO_URL.format(repo=repo)
    token_url = repo_url + "stargazers"
    star_url = repo_url + "star"

    page = session.get(token_url).content.decode()
    tokens = dict(_get_auth_token(page, True))
    authenticity_token = tokens.get('star')
    if authenticity_token:
        data = {'utf8': "✓",
                'authenticity_token': authenticity_token}
        headers = {'Host': HOST,
                   'Referer': repo_url}
        return session.post(star_url, headers=headers, data=data)
    else:
        return False  # Failed attempt


def follow_user(user, session):
    """Follow a github user."""
    user_url = USER_URL.format(user=user)
    follow_url = FOLLOW_URL.format(user=user)
    page = session.get(user_url).content.decode()
    tokens = dict(_get_auth_token(page, True))
    authenticity_token = tokens.get('follow')
    if authenticity_token:
        data = {'utf8': "✓",
                'authenticity_token': authenticity_token}
        headers = {'Host': HOST,
                   'Referer': user_url}
        return session.post(follow_url, headers=headers, data=data)
    else:
        print('Organization couldn\'t be followed...')
        return False  # Failed attempt

def start_scrap_users(topic):
    search_url = 'https://github.com/search?p={page}&q={topic}&type=Users&utf8=%E2%9C%93'
    follows_file = open("res/%s_follows_file.txt" % topic, "w")
    for i in range(1, 101):
        print('------------------第%d页--------------------' % i)
        url = search_url.format(page = i, topic = topic)
        page = s.get(url).content.decode()
        users = _get_all_users(page)
        for i, user in enumerate(users):
            print(user)
            follows_file.write(user + '\n')
        time.sleep(2)
    follows_file.close()


def start_follow_task(topic, session):
    follows_file = open("res/%s_follows_file.txt" % topic, "r")
    lines = follows_file.readlines()
    for user in lines:
        user = user.replace('\n', '')
        print('正在star用户：%s' % user)
        follow_user(user, session)
    follows_file.close()

if __name__ == "__main__":
    s = requests.session()
    username = ''
    password = ''
    login(username, password, s)

    # 1. scrap users, save to file
    # start_scrap_users('ios')
    # 2. read file, then follow users
    # start_follow_task('ios', s)

    # start_scrap_users('python')
    start_follow_task('python', s)
