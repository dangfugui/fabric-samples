#!/bin/bash

COMPOSE_PROJECT_NAME=net
IMAGE_TAG=latest
SYS_CHANNEL=mychannel

MODE=$1
shift
# Determine whether starting, stopping, restarting, generating or upgrading


function networkUp() {
  docker-compose -f docker-compose-cli.yaml up
}


function networkDown() {
  docker-compose -f docker-compose-cli.yaml down --volumes --remove-orphans
  # ##删除所有活跃的容器###
  docker rm -f $(docker ps -aq)
  #docker rm -f $(docker ps -a | grep "hyperledger/*" | awk "{print \$1}")
  ###清理网络缓存###
  docker volume prune
  docker network prune
  docker ps -a
    clearContainers
}

function generateCerts() {
  set -x
  echo "通过  cryptogen工具 生成证书文件"
  cryptogen generate --config=./crypto-config.yaml
  echo "创建创世块文件 -genesis.block"
  configtxgen -profile TwoOrgsOrdererGenesis -channelID byfn-sys-channel -outputBlock ./channel-artifacts/genesis.block

  echo "创建通道文件"
  configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/channel.tx -channelID mychannel
  res=$?
  set +x
  echo "end"
}


if [ "$MODE" == "up" ]; then
  networkUp
elif [ "$MODE" == "down" ]; then
  networkDown
elif [ "$MODE" == "restart" ]; then
  networkDown
  networkUp
elif [ "$MODE" == "generate" ]; then
  generateCerts
else
  printHelp
  exit 1
fi