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
EOT

source ~/.bash_profile

sudo mkdir /deployments
sudo chown -R cloud-user:cloud-user /deployments

unzip -o ~/deployer-*-app.zip -d /tmp
cp -r /tmp/deployer-*-app/* /deployments

echo -e $GREEN"Installed @ /deployments"$WHITE
echo -e $GREEN"ROOT_SCRAPER_DATA == $ROOT_SCRAPER_DATA"$WHITE
echo -e $GREEN"ES_HOME_PATH == $ES_HOME_PATH"$WHITE
