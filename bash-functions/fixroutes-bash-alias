#figures out your current default gateway and adds an entry to your routing table to send all comms through the VPN
addGatewayRoute(){
    sudo route -n add -net 23.35.184.206/32 `netstat -nr | head -6 | tail -1 | cut -c 20-34`
}

alias fixroutes=addGatewayRoute

