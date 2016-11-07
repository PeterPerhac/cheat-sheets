# add this to your .bash_profile to create a wholistens command
whoListensOnPort() {
    if [[ $(($1)) -lt 1000 ]]
    then
        sudo lsof -sTCP:LISTEN -iTCP:$1
    else
        lsof -sTCP:LISTEN -iTCP:$1
    fi
}

alias port=whoListensOnPort

