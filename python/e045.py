#/usr/local/bin/python3

import argparse
parser = argparse.ArgumentParser()
#parser.add_argument("echo", help=u'echo 帮助信息')
#parser.add_argument("square", help="display a square of a given number", type=int)
parser.add_argument("-v", "--verbosity", help="increase output verbosity",
                    action="store_true")
args = parser.parse_args()

if args.verbosity:
    print("verbosity turned on")

