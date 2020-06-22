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
peer chaincode install -p chaincodedev/chaincode/marbles_chaincode -n mycc -v 0 ##log#
sleep 10
#
peer chaincode instantiate -n mycc -v 0 -c '{"Args":[]}' -C myc ##log#
sleep 5
#
#3、初始化弹珠：
#
#分别创建三个弹珠
peer chaincode invoke -n mycc -c '{"Args":["initMarble","1","red","20","Jack"]}' -C myc ##log#
peer chaincode invoke -n mycc -c '{"Args":["initMarble","2","green","66","Wenzil"]}' -C myc ##log#
peer chaincode invoke -n mycc -c '{"Args":["initMarble","3","blue","88","Li Lei"]}' -C myc  ##log#
sleep 5
#
#4、通过弹珠ID查询对应的弹珠信息：
peer chaincode invoke -n mycc -c '{"Args":["getMarbleInfoByID","1"]}' -C myc  ##log#
sleep 5
#
#5、改变弹珠的拥有者：
peer chaincode invoke -n mycc -c '{"Args":["changeMarbleOwner","1","Han Meimei"]}' -C myc ##log#
sleep 5
#
#6、查询改变拥有者后的弹珠信息：
peer chaincode invoke -n mycc -c '{"Args":["getMarbleInfoByID","1"]}' -C myc  ##log#
sleep 5
#
#7、通过弹珠ID删除对应的弹珠：
peer chaincode invoke -n mycc -c '{"Args":["deleteMarbleByID","1"]}' -C myc ##log#
sleep 5
#
#8、查询指定ID范围的弹珠信息：
peer chaincode invoke -n mycc -c '{"Args":["getMarbleByRange","2", "3"]}' -C myc  ##log#
sleep 5
#
#9、查询某个拥有者的所有弹珠信息：
peer chaincode invoke -n mycc -c '{"Args":["getMarbleByOwner","Wenzil"]}' -C myc  ##log#
sleep 5
#
#10、通过弹珠ID查询所有的交易历史信息：
peer chaincode invoke -n mycc -c '{"Args":["getMarbleHistoryForKey","1"]}' -C myc ##log#

set +x
