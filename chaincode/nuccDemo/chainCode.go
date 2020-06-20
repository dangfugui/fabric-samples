package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

var log = shim.NewLogger("chainCode")

func main() {
	log.Info("start main")
}

func tt() {
	log.Info("tt func")
}
