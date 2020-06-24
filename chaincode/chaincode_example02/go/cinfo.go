package main

import (
	"github.com/hyperledger/fabric/core/chaincode/lib/cid"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type CInfo struct {
}

/**
获取用户身份的ou（organization unit，e.g. “org1.department1”）和role（e.g. “member”）
*/
func (t *CInfo) Cinfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// Getting attributes from an idemix credential
	ou, found, err := cid.GetAttributeValue(stub, "ou")
	if err != nil {
		log.Error("Failed to get attribute 'ou'", err)
		//return shim.Error("Failed to get attribute 'ou'")
	}
	log.Infof("user ou: %v\tfound:%v\n", ou, found)
	role, found, err := cid.GetAttributeValue(stub, "role")
	if err != nil {
		//return shim.Error("Failed to get attribute 'role'")
	}
	log.Infof("user role: %v\tfound:%v\n", role, found)
	return shim.Success(nil)
}
