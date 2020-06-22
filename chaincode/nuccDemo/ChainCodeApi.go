package main

import (
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

type ChainCode struct {
}

var log = shim.NewLogger("chainCode")

func (t *ChainCode) Init(stub shim.ChaincodeStubInterface) peer.Response {
	log.Info("chain code init")
	return shim.Success(nil)
}

func (t *ChainCode) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	// Extract the function and args from the transaction proposal
	fn, args := stub.GetFunctionAndParameters()
	log.Info("invoke func:%s  args:%v", fn, args)
	return shim.Success([]byte(nil))
}

// main function starts up the chaincode in the container during instantiate
func main() {
	fmt.Println("ChainCode main start")
	if err := shim.Start(new(ChainCode)); err != nil {
		fmt.Printf("Error starting ChainCode chaincode: %s", err)
	}
	fmt.Println("ChainCode main end")
}
