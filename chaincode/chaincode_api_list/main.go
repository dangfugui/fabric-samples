package main

import (
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

type ChainCodeMain struct {
}

var log = shim.NewLogger("chainCode")

func (t *ChainCodeMain) Init(stub shim.ChaincodeStubInterface) peer.Response {
	log.Info("chain code init")
	fn, args := stub.GetFunctionAndParameters()
	log.Infof("invoke func:%s  args:%v\n", fn, args)
	return shim.Success(nil)
}

func (t *ChainCodeMain) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	// 链初调用
	fn, args := stub.GetFunctionAndParameters()
	log.Infof("invoke func:%s  args:%v\n", fn, args)
	if fn == "get" {
		return Get(stub, args)
	} else if fn == "set" {
		return Set(stub, args)
	} else if fn == "history" {
		return getMarbleHistoryForKey(stub, args)
	} else {
		log.Warning("invoke func:%s   non-existent	_args:%v\n", fn, args)
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
