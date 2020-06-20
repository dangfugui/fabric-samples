#!/bin/bash
echo "通过  cryptogen工具 生成证书文件"
cryptogen generate --config=./crypto-config.yaml
echo "创建创世块文件 -genesis.block"
configtxgen -profile TwoOrgsOrdererGenesis --configPath ./ -channelID nuccchannel -outputBlock ./channel-artifacts/genesis.block

echo "创建通道文件"
configtxgen -profile TwoOrgsChannel --configPath ./ -outputCreateChannelTx ./channel-artifacts/nuccchannel.tx -channelID nuccchannel
### 列出通道命令  peer channel list
#echo "锚节点修改 提案  如果没有变更configtx.yaml里的 锚节点  可以不做"
#configtxgen -profile OrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org1MSPanchors.tx -channelID nuccchannel -asOrg Org1MSP
#configtxgen -profile OrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org2MSPanchors.tx -channelID nuccchannel -asOrg Org2MSP
#configtxgen -profile OrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org3MSPanchors.tx -channelID nuccchannel -asOrg Org3MSP


echo "end"