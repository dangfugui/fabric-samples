package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

/**
投票系统 https://github.com/didianV5/voteApp/blob/master/chaincode/vote/vote.go
*/
type VoteChaincode struct {
}

type Vote struct {
	Username string `json:"username"`
	VoteNum  int    `json:"votenum""`
}

func (t *VoteChaincode) Init(stub shim.ChaincodeStubInterface) peer.Response {
	return shim.Success(nil)
}

func (t *VoteChaincode) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	fn, args := stub.GetFunctionAndParameters()
	if fn == "voteUser" {
		return t.voteUser(stub, args)
	} else if fn == "getUserVote" {
		return t.getUserVote(stub, args)
	}
	return shim.Error("调用方法不存在")
}

func (t *VoteChaincode) voteUser(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	fmt.Println("start voteUser")
	username := args[0]
	userAsBytes, err := stub.GetState(username)
	if err != nil {
		fmt.Errorf("stub.GetState(%s) error", username)
		return shim.Error(err.Error())
	}
	vote := Vote{}
	if userAsBytes != nil {
		err = json.Unmarshal(userAsBytes, &vote)
		if err != nil {
			fmt.Errorf("json.Unmarshal(%s,&vote)", userAsBytes)
			return shim.Error(err.Error())
		}
	} else {
		vote = Vote{Username: username, VoteNum: 1}
	}
	voteJsonAsBytes, err := json.Marshal(vote)
	if err != nil {
		fmt.Errorf("json.Marshal(vote)")
		return shim.Error(err.Error())
	}
	err = stub.PutState(username, voteJsonAsBytes)
	fmt.Println("end voteUser:" + username)
	return shim.Success(nil)
}

func (t *VoteChaincode) getUserVote(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	fmt.Println("start getUserVote")

	resultIterator, err := stub.GetStateByRange("", "")
	if err != nil {
		fmt.Errorf("stub.GetStateByRange(\"\",\"\")")
		return shim.Error(err.Error())
	}
	defer resultIterator.Close()
	var buffer bytes.Buffer
	buffer.WriteString("[")
	isWrite := false
	for resultIterator.HasNext() {
		queryResult, err := resultIterator.Next()
		if err != nil {
			fmt.Errorf("resultIterator.Next()")
			return shim.Error(err.Error())
			if isWrite == true {
				buffer.WriteString(",")
			}
			buffer.WriteString(string(queryResult.Value))
		}
		buffer.WriteString("]")
	}
	fmt.Println("end getUserVote:" + buffer.String())
	return shim.Success(buffer.Bytes())
}

func main() {
	err := shim.Start(new(VoteChaincode))
	if err != nil {
		fmt.Println("chaincode start eror")
	}
}
