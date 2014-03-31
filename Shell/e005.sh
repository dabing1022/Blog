#!/bin/bash
# dir root_dir
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ROOT_DIR="`dirname $DIR`"
LUAJIT_SRC_DIR="$ROOT_DIR/lib/cocos2d-x/scripting/lua/luajit/LuaJIT-2.0.2"

echo ${BASH_SOURCE[0]}
echo $(dirname "${BASH_SOURCE[0]}")
echo $DIR
echo $ROOT_DIR

ROOT_ROOT_DIR="$(dirname $ROOT_DIR)"
# ROOT_ROOT_DIR="`dirname $ROOT_DIR`"

echo $ROOT_ROOT_DIR
echo $LUAJIT_SRC_DIR

