package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"testing"
)

type TestUtil struct {
	stub *shim.MockStub
}

func NewTestUtil() *TestUtil {
	util := new(TestUtil)
	stub := shim.NewMockStub("ex02", new(ChainCodeMain))
	util.stub = stub
	util.TestInit()
	return util
}

func (tu *TestUtil) TestInit() peer.Response {
	fmt.Println("start test init")
	args := [][]byte{[]byte("testInit"), []byte("args1")}
	res := tu.stub.MockInit("1", args)
	if res.Status != shim.OK {
		fmt.Println("Init", "failed", string(res.Message))
	}
	return res
}

// 测试chaincode 调用方法
func (tu *TestUtil) TestInvoke(args [][]byte) peer.Response {
	fmt.Println("## start test invoke")
	res := tu.stub.MockInvoke("1", args)
	fmt.Println("## test invoke end res:", res)
	return res
}

func (tu *TestUtil) TestInvokeStr(args []string) peer.Response {
	var btArgs [][]byte
	//args1 := [][]byte{[]byte("set"), []byte("dd"),[]byte("value")}
	for j := 0; j < len(args); j++ {
		btArgs = append(btArgs, []byte(args[j]))
	}
	return tu.TestInvoke(btArgs)
}

//  6 static public void assertEquals(String message, Object expected, Object actual) {
func (tu *TestUtil) AssertEqual(t *testing.T, expected, actual interface{}, message string) {
	if expected != actual {
		t.Errorf("Not Equal. %d %d || %v", expected, actual, message)
	} else {
		fmt.Println("## assert equal:", actual)
	}
}
