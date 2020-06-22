package main

import (
	"fmt"
	"testing"
)

func TestExample01_Set(t *testing.T) {
	test := NewTestUtil()
	// 分别创建三个弹珠
	res := test.TestInvokeStr([]string{"initMarble", "1", "red", "20", "Jack"})
	res = test.TestInvokeStr([]string{"initMarble", "2", "green", "66", "Wenzil"})
	res = test.TestInvokeStr([]string{"initMarble", "3", "blue", "88", "Li Lei"})

	//4、通过弹珠ID查询对应的弹珠信息：
	//peer chaincode invoke -n mycc -c '{"Args":["getMarbleInfoByID","1"]}' -C myc
	res = test.TestInvokeStr([]string{"getMarbleInfoByID", "1"})
	//5、改变弹珠的拥有者：
	//peer chaincode invoke -n mycc -c '{"Args":["changeMarbleOwner","1","Han Meimei"]}' -C myc
	res = test.TestInvokeStr([]string{"changeMarbleOwner", "1", "Han Meimei"})
	//6、查询改变拥有者后的弹珠信息：
	//peer chaincode invoke -n mycc -c '{"Args":["getMarbleInfoByID","1"]}' -C myc
	res = test.TestInvokeStr([]string{"getMarbleInfoByID", "1"})
	//7、通过弹珠ID删除对应的弹珠：
	//peer chaincode invoke -n mycc -c '{"Args":["deleteMarbleByID","1"]}' -C myc
	res = test.TestInvokeStr([]string{"deleteMarbleByID", "1"})
	//8、查询指定ID范围的弹珠信息：
	//peer chaincode invoke -n mycc -c '{"Args":["getMarbleByRange","2", "3"]}' -C myc
	res = test.TestInvokeStr([]string{"getMarbleByRange", "2", "3"})
	//9、查询某个拥有者的所有弹珠信息：
	//peer chaincode invoke -n mycc -c '{"Args":["getMarbleByOwner","Wenzil"]}' -C myc
	res = test.TestInvokeStr([]string{"getMarbleByOwner", "Wenzil"})
	//10、通过弹珠ID查询所有的交易历史信息：
	//peer chaincode invoke -n mycc -c '{"Args":["getMarbleHistoryForKey","1"]}' -C myc
	res = test.TestInvokeStr([]string{"getMarbleHistoryForKey", "1"})
	fmt.Println(res)

}
