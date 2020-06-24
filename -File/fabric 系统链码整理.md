##Hyperledger Fabric v1.1 中的系统链码
```textmate
Hyperledger Fabric v1.1提供了多种特殊的链码，称为系统链码，以执行某些特权任务。本文的目的是为Fabric中各种系统链代码的实现，其功能和用法提供指针。像用户链代码一样，系统链代码也实现Init（）和Invoke（）函数。有五个系统链码，下面列出：
配置系统链码（CSCC）-core / scc / cscc / configure.go
循环系统链码（LSCC）-core / scc / lscc / lscc.go
查询系统链码（QSCC）-core / scc / qscc / query.go
Endorser系统链码（ESCC）-core / scc / escc / endorser_onevalidsignature.go
验证者系统链码（VSCC）-core / scc / vscc / validator_onevalidsignature.go
接下来，我们介绍这些系统链码中的每一个所提供的功能以及用法。请注意，由于我们可能需要传递golang结构的某些序列化protobuf字节，因此我们可能无法使用命令行（CLI）轻松调用/查询系统链码支持的所有功能。因此，对于此类功能，建议使用SDK。在本文中，我们仅使用CLI对适用的函数执行调用/查询。
```

### 1.配置系统链码（CSCC）Configuration System Chaincode
```shell script
##### JoinChain       调用JoinChain功能以使对等方加入频道
$ peer channel join -b ch1.block
##### GetConfigBlock  调用GetConfigBlock以获得给定通道的当前配置块
$ peer chaincode query -C "" -n cscc -c '{"Args":["GetConfigBlock", "ch1"]}'
  (or)
$ peer channel fetch -o orderer0:7050 config -c ch1
##### 调用GetChannels以获取有关对等方到目前为止已加入的所有通道的信息
$ peer chaincode query -C "" -n cscc -c '{"Args":["GetChannels"]}'
  (or)
$ peer channel list
```
--------------------------------------------------------------------------------
### 2.循环系统链码（LSCC ）Life Cycle System Chaincode -core / scc / lscc / lscc.go
```shell script
### (i) install 安装
$ peer chaincode install -n generic-chaincode -v 1.0 -p github.com/hyperledger/fabric/examples/chaincode/go/generic-chaincode
### (ii) deploy,   部署
$ peer chaincode instantiate -o orderer0:7050 -C ch1 -n generic-chaincode -v 1.0 -c '{"Args":["init"]}' -P "AND ('Org0MSP.member','Org1MSP.member', 'Org2MSP.member', 'Org3MSP.member')"
### (iii) upgrade, 升级

### (iv) getid, 
###(v) getdepspec,  获取安装在对等方上的链码的部署规范
$ peer chaincode query -C "mychannel" -n lscc -c '{"Args":["getdepspec", "mychannel", "mycc"]}'
### (vi) getccdata, 
### (vii) getchaincodes, and 
$ peer chaincode query -C "mychannel" -n lscc -c '{"Args":["getchaincodes"]}'
### (viii) getinstalledchaincodes.  获取peer上已安装链码的列表
$ peer chaincode query -C "mychannel" -n lscc -c '{"Args":["getinstalledchaincodes"]}'
```

### 3.查询系统链码（QSCC）Query System Chaincode -core / scc / qscc / query.go

```shell script
$ peer chaincode query -C "mychannel" -n mycc -c '{"Args":["get", "a"]}'
```

### 4. Endorser系统链码  Endorser System Chaincode (ESCC)  -core / scc / escc / endorser_onevalidsignature.go
执行交易后，背书对等方（core / endorser / endorser.go）调用ESCC，以将其签名放在交易响应消息上，该消息包括交易执行的结果（即交易状态，链码事件，读取/写集）。调用函数可以接受5到7个参数。这些参数是Header，ChaincodeProposalPayload，ChaincodeID，响应，模拟结果，事件和有效负载可见性。

### 5. 验证者系统链码 Validator System Chaincode (VSCC) -core / scc / vscc / validator_onevalidsignature.go
提交对等方（core / committer / txvalidator / validator.go）调用VSCC，以根据链码的认可策略验证每个交易的签名集。






#### 相关参考
```textmate
 系统合约
https://guanpengchn.gitbooks.io/lazy-blog/content/blog/fabriczhong-de-lscc.html
 背书策略 key level 背书策略  特性调研之Identity Mixer  运维信息监控
https://www.jianshu.com/u/24f06408a2a9
```

#### 环签名相关关键字
zkp
Identity Mixer
https://blog.csdn.net/yohjob/article/details/103619649