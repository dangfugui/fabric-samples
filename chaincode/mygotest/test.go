package main

import "fmt"

type Sample struct {
	a   int
	str string
}

func main() {
	s := Sample{a: 1, str: "hello"}

	fmt.Printf("%v\n", s)    //{1, hello}
	fmt.Printf("%+v\n", s)   //{a:1, str:hello}
	fmt.Printf("%#v\n", s)   //main.Sample{a:1, str:"hello"}
	fmt.Printf("%T\n", s)    // main.Sample
	fmt.Printf("%% \n", s.a) // %  %!(EXTRA int=1)
	i := 1
	str := "string"
	fmt.Printf("i: %#v   s:%+v\n", i, str)
	arr := []string{"a", "b"}
	fmt.Printf("%v\n", arr)

}
