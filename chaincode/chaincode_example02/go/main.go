package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

type ChainCodeMain struct {
}

var log = shim.NewLogger("chainCode")
var state = new(State)
var asset = new(Asset)
var cinfo = new(CInfo)

func (t *ChainCodeMain) Init(stub shim.ChaincodeStubInterface) peer.Response {
	log.Info("chain code init")
	fn, args := stub.GetFunctionAndParameters()
	log.Infof("invoke func:%s  args:%v\n", fn, args)
	return asset.Init(stub, args)
}

func (t *ChainCodeMain) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	// 链初调用
	fn, args := stub.GetFunctionAndParameters()
	log.Infof("invoke func:%s  args:%v\n", fn, args)
	/////////////////////////////////////  state   ////////////////////////////////////////////////////
	if fn == "get" { // 获取state值
		return state.Get(stub, args)
	} else if fn == "set" { // 设置state值
		return state.Set(stub, args)
	} else if fn == "history" { // 获取 state历史值
		return state.MarbleHistoryForKey(stub, args)
		/////////////////////////////////////  asset   ////////////////////////////////////////////////////
	} else if fn == "query" { // 查询资产
		return asset.Query(stub, args)
	} else if fn == "invoke" { // 转账
		return asset.Invoke(stub, args)
	} else if fn == "create" { // 创建资产
		return asset.Set(stub, args)
	} else if fn == "delete" { // 删除资产
		return asset.Delete(stub, args)
		/////////////////////////////////////  state   ////////////////////////////////////////////////////
	} else if fn == "cinfo" {
		return cinfo.Cinfo(stub, args)
	} else {
		log.Warningf("invoke func:%s   non-existent	_args:%v\n", fn, args)
		return shim.Error("调用方法不存在")
		return shim.Success([]byte(nil))
	}
}

// main function starts up the chaincode in the container during instantiate
func main() {
	log.Info("ChainCode main start")
	if err := shim.Start(new(ChainCodeMain)); err != nil {
		fmt.Printf("Error starting ChainCode chaincode: %s", err)
	}
	log.Infof("ChainCode main end")
}
