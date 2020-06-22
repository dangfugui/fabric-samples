#!/bin/bash

set -x
SCRIPTSPATH="marbles_chaincode/scripts"

#### 4.1 终端1 - 开启网络
#```text
####删除所有活跃的容器###
docker-compose -f ./../../../chaincode-docker-devmode/docker-compose-simple.yaml down
#docker rm -f $(docker ps -aq)
####清理网络缓存###
#docker network prune
####开启网络###
docker-compose -f ./../../../chaincode-docker-devmode/docker-compose-simple.yaml up -d
sleep 5
#```
#### 4.1 终端2 - 编译和运行链码
#```text
####进入Docker容器cli###    docker exec cli scripts/script.sh $CHANNEL_NAME $CLI_DELAY $LANGUAGE $CLI_TIMEOUT $VERBOSE $NO_CHAINCODE
#docker exec chaincode bash  marbles_chaincode/scripts/chaincode.sh
docker exec chaincode  bash  "$SCRIPTSPATH/chaincode.sh"
sleep 10

# docker exec  cli bash chaincode/marbles_chaincode/scripts/cli.sh
docker exec  cli bash "chaincode/$SCRIPTSPATH/cli.sh"


set +x
echo
echo " _____   _   _   ____   "
echo "| ____| | \ | | |  _ \  "
echo "|  _|   |  \| | | | | | "
echo "| |___  | |\  | | |_| | "
echo "|_____| |_| \_| |____/  "
echo

##链接：https://www.jianshu.com/p/c9b5d0bcd0d6。
