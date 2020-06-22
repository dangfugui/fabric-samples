package main

import (
	"bytes"
	"encoding/json"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Set stores the asset (both key and value) on the ledger. If the key exists,
// it will override the value with the new one
func Set(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 2 {
		log.Warning("Incorrect arguments. Expecting a key and a value")
		return shim.Error("Incorrect arguments. Expecting a key and a value")
	}
	err := stub.PutState(args[0], []byte(args[1]))
	if err != nil {
		log.Warningf("Failed to set asset: %s \n", args[0])
		return shim.Error("Failed to set asset:" + args[0])
	}
	creator, _ := stub.GetCreator()
	log.Infof("set %v = %v  by %v", args[0], args[1], string(creator))
	return shim.Success([]byte(args[1]))
}

// Get returns the value of the specified asset key
func Get(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		log.Warning("Incorrect arguments. Expecting a key and a value\n")
		return shim.Error("Incorrect arguments. Expecting a key and a value")
	}

	value, err := stub.GetState(args[0])
	if err != nil {
		log.Warningf("Failed to get asset: %s with error: %s\n", args[0], err.Error())
		return shim.Error("Failed to get asset:" + err.Error())
	}
	if value == nil {
		log.Warningf("Asset not found: %s\n", args[0])
	}
	return shim.Success(value)
}

// 通过弹珠ID查询所有的交易历史信息
func getMarbleHistoryForKey(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	marbleId := args[0]
	// 返回某个键的所有历史值
	resultsIterator, err := stub.GetHistoryForKey(marbleId)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()
	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			log.Error(err.Error())
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		item, _ := json.Marshal(queryResponse)
		buffer.Write(item)
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
	log.Infof("-getMarbleHistoryForKey queryResult:\n%s\n", buffer.String())
	return shim.Success(buffer.Bytes())
}
