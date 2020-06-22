#!/bin/bash

#### 4.3 终端3 - 安装和调用链码
#```text
#### 1、启动Docker CLI容器：
#docker exec -it cli bash
#2、安装和实例化链码：


echo "==============================chaincode install============================"
echo "==============================chaincode install============================"
echo "==============================chaincode install============================"

set -x
peer chaincode install -p chaincodedev/chaincode/chaincode_api_list -n mycc -v 0 ##log#
sleep 10
#
peer chaincode instantiate -n mycc -v 0 -c '{"Args":[]}' -C myc ##log#
sleep 5
peer chaincode invoke -n mycc -c '{"Args":["set","a","a1"]}' -C myc
peer chaincode invoke -n mycc -c '{"Args":["set","b","b1"]}' -C myc
peer chaincode invoke -n mycc -c '{"Args":["set","c","c1"]}' -C myc
sleep 5
peer chaincode invoke -n mycc -c '{"Args":["set","a","a2"]}' -C myc
sleep 2
peer chaincode invoke -n mycc -c '{"Args":["get","a"]}' -C myc
sleep 5
peer chaincode invoke -n mycc -c '{"Args":["history","a"]}' -C myc

echo
echo "==============================cli end============================"