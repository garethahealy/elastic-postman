#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Installing scraper"$WHITE

echo -e $GREEN"Setting env vars"$WHITE

cat >> ~/.bash_profile <<EOT

export ROOT_SCRAPER_DATA=/deployments/data
export ES_HOME_PATH=/deployments/elastic
export JAVA_OPTIONS="-server -Xms1GB -Xmx1GB -XX:+UnlockDiagnosticVMOptions -XX:+UnsyncloadClass"
EOT

source ~/.bash_profile

sudo mkdir /deployments
sudo chown -R cloud-user:cloud-user /deployments

unzip -o ~/deployer-*-app.zip -d /tmp
cp -r /tmp/deployer-*-app/* /deployments

echo -e $GREEN"Installed @ /deployments"$WHITE
echo -e $GREEN"ROOT_SCRAPER_DATA == $ROOT_SCRAPER_DATA"$WHITE
echo -e $GREEN"ES_HOME_PATH == $ES_HOME_PATH"$WHITE

echo -e $GREEN"Starting elastic postman as systemd service"$WHITE

sudo bash -c "cat >> /etc/systemd/system/elastic-postman.service <<EOT
[Unit]
Description=Elastic Postman

[Service]
Type=simple
ExecStart=/deployments/bin/run.sh
User=cloud-user
Group=root

[Install]
WantedBy=multi-user.target
EOT"

sudo mkdir -p /etc/systemd/system/elastic-postman.service.d
sudo bash -c "cat >> /etc/systemd/system/elastic-postman.service.d/local.conf <<EOT
[Service]
Environment="ROOT_SCRAPER_DATA=/deployments/data"
Environment="ES_HOME_PATH=/deployments/elastic"
Environment="JAVA_OPTIONS='-server -Xms1GB -Xmx1GB -XX:+UnlockDiagnosticVMOptions -XX:+UnsyncloadClass'"
EOT"

sudo systemctl daemon-reload
sudo systemctl enable elastic-postman.service
sudo systemctl status elastic-postman.service
