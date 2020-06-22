#!/bin/bash

set +x
echo "==============================start chaincode============================"
echo "==============================start chaincode============================"
echo "==============================start chaincode============================"


cd marbles_chaincode
###编译链码###
go build
###启动Peer节点运行链码###
CORE_PEER_ADDRESS=peer:7052 CORE_CHAINCODE_ID_NAME=mycc:0 ./marbles_chaincode

echo "==============================end chaincode============================"
echo "==============================end chaincode============================"
echo "==============================end chaincode============================"
###如果失败，把"7052"改为"7051"试试看
