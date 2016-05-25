#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Configing Kibana for YUM install"$WHITE

sudo rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch

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
sudo systemctl status kibana.service

sudo chown -R kibana:root /opt/kibana

sudo openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem -subj "/C=UK/ST=England/L=London/O=RHC OS1/OU=Consulting/CN=elastic-postman"
sudo mv *.pem /opt/kibana/config/
sudo chown kibana:root /opt/kibana/config/*.pem

sudo bash -c "cat >> /opt/kibana/config/kibana.yml <<EOT
server.ssl.cert: /opt/kibana/config/cert.pem
server.ssl.key: /opt/kibana/config/key.pem
EOT"


