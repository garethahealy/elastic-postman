#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Configing firewall"$WHITE

# Enable firewall
sudo yum install -y firewalld

sudo systemctl enable firewalld
sudo systemctl start firewalld
sudo systemctl status firewalld

echo -e $GREEN"Opening: 5601 (kibana), 9001 (jetty)"$WHITE

sudo firewall-cmd --zone=public --add-port=5601/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9001/tcp --permanent
sudo firewall-cmd --reload

sudo firewall-cmd --get-zones
sudo firewall-cmd --permanent --zone=public --list-all
