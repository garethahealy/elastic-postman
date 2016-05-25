#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Disabling repos"$WHITE

sudo subscription-manager repos --disable="*" && \
    sudo subscription-manager repos \
        --enable="rhel-7-server-rpms" \
        --enable="rhel-7-server-extras-rpms" \
        --enable="rhel-ha-for-rhel-7-server-rpms" \
        --enable="rhel-7-server-ose-3.1-rpms"

echo -e $GREEN"Updating system"$WHITE

sudo yum makecache && \
    sudo yum install -y deltarpm && \
    sudo yum update -y && \
    sudo yum install -y gcc kernel-devel make && \
    sudo yum install -y wget vim zip unzip bash-completion
