#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

USERNAME=$1
PASSWORD=$2

echo -e $GREEN"Registering for: $USERNAME"$WHITE

sudo subscription-manager register --username $USERNAME --password $PASSWORD

# https://lists.openshift.redhat.com/openshift-archives/users/2015-April/msg00021.html
# Select the last employee SKU and hope its "good"
POOL_ID=$(sudo subscription-manager list --available | sed -n '/Employee SKU/,/System Type/p' | grep "Pool ID" | tail -1 | cut -d':' -f2 | xargs)

echo -e $GREEN"Trying PoolID: $POOL_ID"$WHITE

sudo subscription-manager attach --pool=$POOL_ID

sudo yum repolist
