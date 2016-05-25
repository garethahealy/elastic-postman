#!/usr/bin/env bash

# Set colours
GREEN="\e[32m"
RED="\e[41m\e[37m\e[1m"
YELLOW="\e[33m"
WHITE="\e[0m"

echo -e $GREEN"Installing java packages"$WHITE

sudo yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel

cat >> ~/.bash_profile <<EOT

JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk/
export JAVA_HOME
EOT

source ~/.bash_profile

echo -e $GREEN"JAVA_HOME == $JAVA_HOME"$WHITE
echo -e $GREEN"$(java -version)"$WHITE
echo -e $GREEN"$(javac -version)"$WHITE
