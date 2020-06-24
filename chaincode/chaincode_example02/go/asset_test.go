package main

import (
	"testing"
)

func TestExample01_Set22(t *testing.T) {
	test := NewTestUtil()
	res := test.TestInvokeStr([]string{"query", "a"}) // 查询 a 资产
	test.AssertEqual(t, "100", string(res.Payload), "a 的资产")
	res = test.TestInvokeStr([]string{"invoke", "a", "b", "10"}) // a 向b 转账10
	res = test.TestInvokeStr([]string{"query", "a"})             // 查询 a 资产
	test.AssertEqual(t, "90", string(res.Payload), "a 的资产")
	res = test.TestInvokeStr([]string{"delete", "a"}) // 删除a的资产
	res = test.TestInvokeStr([]string{"query", "a"})  // 查询 a 资产
	test.AssertEqual(t, int32(500), res.Status, "预期查询失败 状态500")
}
