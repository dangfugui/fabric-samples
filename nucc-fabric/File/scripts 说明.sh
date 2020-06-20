##########################################################
##### Generate certificates using cryptogen tool  #########
#############通过  cryptogen工具 生成证书文件###############
ryptogen generate --config=./crypto-config.yaml

##########################################################
#########  Generating Orderer Genesis block ##############
#############创建创世块文件 -genesis.block ################

configtxgen -profile TwoOrgsOrdererGenesis -channelID byfn-sys-channel -outputBlock ./channel-artifacts/genesis.block


#################################################################
### Generating channel configuration transaction 'channel.tx' ###
#################创建通道文件         ############################
configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/channel.tx -channelID mychannel

#################################################################
#######    Generating anchor peer update for Org1MSP   ##########
############锚节点修改 提案  如果没有变更configtx.yaml里的 锚节点  可以不做###########################
configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org1MSPanchors.tx -channelID mychannel -asOrg Org1MSP
configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org2MSPanchors.tx -channelID mychannel -asOrg Org2MSP

## Channel name : mychannel cli 创建通道
peer channel create -o orderer.example.com:7050 -c mychannel -f ./channel-artifacts/channel.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
## 加入通道
peer channel join -b mychannel.block
+ peer channel join -b mychannel.block
+ peer channel join -b mychannel.block
+ peer channel join -b mychannel.block
# ===================== peer1.org2 joined channel 'mychannel' =====================
# 更新锚节点
peer channel update -o orderer.example.com:7050 -c mychannel -f ./channel-artifacts/Org1MSPanchors.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
peer channel update -o orderer.example.com:7050 -c mychannel -f ./channel-artifacts/Org2MSPanchors.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
# ===================== Anchor peers updated for org 'Org2MSP' on channel 'mychannel' =====================

# 安装链码
peer chaincode install -n mycc -v 1.0 -l golang -p github.com/chaincode/chaincode_example02/go/
+ peer chaincode install -n mycc -v 1.0 -l golang -p github.com/chaincode/chaincode_example02/go/
# ===================== Chaincode is installed on peer0.org2 =====================

# 初始化链码 Instantiating chaincode on peer0.org2... #
peer chaincode instantiate -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc -l golang -v 1.0 -c '{"Args":["init","a","100","b","200"]}' -P 'AND ('\''Org1MSP.peer'\'','\''Org2MSP.peer'\'')'
# ===================== Chaincode is instantiated on peer0.org2 on channel 'mychannel' =====================

# ========链码查询============= Querying on peer0.org1 on channel 'mychannel'... =====================
peer chaincode query -C mychannel -n mycc -c '{"Args":["query","a"]}'
# 100
# 链码 调用 Sending invoke transaction on peer0.org1 peer0.org2...
+ peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["invoke","a","b","10"]}'
# ===================== Invoke transaction successful on peer0.org1 peer0.org2 on channel 'mychannel' =====================
peer chaincode install -n mycc -v 1.0 -l golang -p github.com/chaincode/chaincode_example02/go/
# ===================== Chaincode is installed on peer1.org2 =====================

# ===================== Querying on peer1.org2 on channel 'mychannel'... =====================
peer chaincode query -C mychannel -n mycc -c '{"Args":["query","a"]}'
90
#===================== Query successful on peer1.org2 on channel 'mychannel' =====================
#========= All GOOD, BYFN execution completed ===========


