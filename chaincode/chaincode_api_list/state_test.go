package main

import (
	"fmt"
	"testing"
	"time"
)

func TestExample01_Set(t *testing.T) {
	key := "key_a"
	value := "a1"
	test := NewTestUtil()

	args := [][]byte{[]byte("set"), []byte(key), []byte(value)}
	res := test.TestInvoke(args)
	test.AssertEqual(t, value, string(res.Payload), "set 返回值不符")

	args = [][]byte{[]byte("get"), []byte(key)}
	res = test.TestInvoke(args)
	fmt.Println(res)
	test.AssertEqual(t, value, string(res.Payload), "get value 返回值不符")

	value = "a2"
	args = [][]byte{[]byte("set"), []byte(key), []byte(value)}
	res = test.TestInvoke(args)
	test.AssertEqual(t, value, string(res.Payload), "set 返回值不符")

	args = [][]byte{[]byte("history"), []byte(key)}
	res = test.TestInvoke(args)
	fmt.Println(res)
	test.AssertEqual(t, value, string(res.Payload), "get value 返回值不符")
}

func TestExample01_Set2(t *testing.T) {
	test := NewTestUtil()
	res := test.TestInvokeStr([]string{"set", "a", "a1"})
	res = test.TestInvokeStr([]string{"set", "b", "b1"})
	res = test.TestInvokeStr([]string{"set", "c", "c1"})
	res = test.TestInvokeStr([]string{"set", "a", "a2"})
	time.Sleep(10 * 000)
	res = test.TestInvokeStr([]string{"get", "a"})
	test.AssertEqual(t, "a2", string(res.GetPayload()), "")
	fmt.Println(res)
	res = test.TestInvokeStr([]string{"history", "a"})
	fmt.Println(res)
}

// go test -v vote_test.go vote.go
func TestExample03_Invoke(t *testing.T) {
	fmt.Println("TestExample02_Invoke")
	//checkInvoke(t,stub,[][]byte{[]byte("voteUser"),[]byte("")})
}
