#!/bin/bash

docker-compose -f docker-compose-cli.yaml down --volumes --remove-orphans

docker-compose -f docker-compose-org1-peer0.yaml down --volumes --remove-orphans
docker-compose -f docker-compose-org1-peer1.yaml down --volumes --remove-orphans
docker-compose -f docker-compose-org2-peer0.yaml down --volumes --remove-orphans
docker-compose -f docker-compose-org2-peer1.yaml down --volumes --remove-orphans
docker-compose -f docker-compose-org3-peer0.yaml down --volumes --remove-orphans
docker-compose -f docker-compose-org3-peer1.yaml down --volumes --remove-orphans


docker-compose -f docker-compose-order.yaml down --volumes --remove-orphans

# ##删除所有活跃的容器###
docker rm -f $(docker ps -aq)
#docker rm -f $(docker ps -a | grep "hyperledger/*" | awk "{print \$1}")
###清理网络缓存###
docker volume prune

docker network prune


docker ps -a