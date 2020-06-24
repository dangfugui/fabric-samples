package main

import (
	"fmt"
	"testing"
)

func TestUinfo(t *testing.T) {
	test := NewTestUtil()
	res := test.TestInvokeStr([]string{"cinfo"}) // 查询 a 资产
	fmt.Println(res)
}
