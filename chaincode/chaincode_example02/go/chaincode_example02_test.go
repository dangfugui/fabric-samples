package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"testing"
)

func checkInvoke(t *testing.T, stub *shim.MockStub, args [][]byte) {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invote", "failed", string(res.Message))
		t.FailNow()
	}
}

// go test -v vote_test.go vote.go
func Te1stExample02_Invoke(t *testing.T) {
	scc := new(SimpleChaincode)
	stub := shim.NewMockStub("ex02", scc)
	checkInvoke(t, stub, [][]byte{[]byte("set"), []byte("a"), []byte("1")})
	//checkInvoke(t,stub,[][]byte{[]byte("voteUser"),[]byte("")})

}
