#!/bin/bash
set -e

# ----------------------
# 1. Swap 설정 (1GB)
# ----------------------
fallocate -l 1G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile
echo "/swapfile swap swap defaults 0 0" >> /etc/fstab

# ----------------------
# 2. Docker 설치
# ----------------------
yum update -y
amazon-linux-extras install docker -y
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

# ----------------------
# 3. Docker Compose 설치
# ----------------------
DOCKER_COMPOSE_VERSION="v2.24.0"
curl -SL https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose