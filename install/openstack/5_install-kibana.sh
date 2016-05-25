#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Configing Kibana for YUM install"$WHITE

sudo rpm --import https://packages.elastic.co/D88E42B4-elasticsearch

sudo bash -c "cat >> /etc/yum.repos.d/kibana.repo <<EOT
[kibana-4.5]
name=Kibana repository for 4.5.x packages
baseurl=http://packages.elastic.co/kibana/4.5/centos
gpgcheck=1
gpgkey=http://packages.elastic.co/GPG-KEY-elasticsearch
enabled=1
EOT"

echo -e $GREEN"Installing Kibana"$WHITE

sudo yum install -y kibana

sudo systemctl daemon-reload
sudo systemctl enable kibana.service
sudo systemctl start kibana.service
sudo systemctl status kibana.service

netstat -an | grep 5601
