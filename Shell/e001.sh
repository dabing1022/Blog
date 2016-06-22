#!/bin/bash
# read and echo
clear
declare name
echo ""
echo "Enter your name:"
read name
echo "Your name is ${name}"

echo $SHELL

if [ $UID -ne 0 ]; then
  echo Non root user.
else
  echo root user.
fi
