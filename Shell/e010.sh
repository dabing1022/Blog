#!/bin/bash
# function

function display
{
    echo "Welcome to the world!"
    echo "Let's start learning how to write a function"
}
display


# pass arguments
clear
function varify
{
    if [ $# -ne 2 ]
        then
            echo "Wrong number of arguments"
    else
        if [ $1 -eq "Bob" ] && [ $2 -eq "555" ]
            then
                echo "Varified"
        else
                echo "Rejected"
        fi
    fi
}
varify "Bob" "555"
