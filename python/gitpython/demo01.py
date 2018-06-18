#! python3
# coding:utf-8

from git import Repo
import git

repo_path = "/Users/ChildhoodAndy/ZZPods/SDWebImage"
# rorepo is a Repo instance pointing to the git-python repository.
# For all you know, the first argument to Repo is a path to the repository
# you want to work with
repo = Repo(repo_path)

class MyProgressPrinter(git.remote.RemoteProgress):
    def update(self, op_code, cur_count, max_count=None, message=''):
        print(op_code, cur_count, max_count, cur_count / (max_count or 100.0), message or "NO MESSAGE")

class Progress(git.remote.RemoteProgress):
    def line_dropped(self, line):
        print(line)
    def update(self, *args):
        pass
        # print(self._cur_line)

# Repo.clone_from("git@gitlab.zhuanspirit.com:zz-ios/Protobuf-Files.git", "/Users/ChildhoodAndy/Documents/Protobuf", progress=Progress())

# print(repo.heads)
# print(repo.head)
#
branch = repo.head.reference
print(branch)
#
# commit = branch.commit
# print(commit)
#
# log = branch.log()
# print(log)
# print(log[0])
# print(log[-1])
#
# tree = repo.heads.master.commit.tree
# print(tree)

o = repo.remotes.origin
# o.pull(progress=MyProgressPrinter())
o.pull(progress=Progress())
