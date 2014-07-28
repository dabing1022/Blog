// Playground - noun: a place where people can play

//array dictionary

var shoppingList = ["aa", "bb", "cc"]

shoppingList += "dd"
shoppingList += ["ee", "ff", "gg"]
shoppingList[0] = "mm"
shoppingList[1...3] = ["11", "22"] //1、2、3区间的元素被等号后边的数组替代
shoppingList

var numberOfLegs = ["ant":6, "snake":0, "dog":4, "cat":4]
numberOfLegs["snake"] = 200
numberOfLegs["ant"] = 100
numberOfLegs

var number: Int? = numberOfLegs["cat"]
//if number == nil {
//    println("there is no key named 'cat'")
//} else {
//    let legCount = number!
//    println("cat legs num:\(legCount) legs")
//}

if let legCount = number {
    println("cat legs num:\(legCount) legs")
    legCount
}

//function
func sayHello(name:String) {
    println("\(name), hello!")
}

sayHello("ChildhoodAndy")

func sayHello2(name:String = "Andy") {
    println("\(name), hello!")
}

sayHello2()
sayHello2(name:"AndyLau")

//------------return value
func buildGreeting(name:String = "person1") -> String {
    return name + ", hello!"
}

let greeting = buildGreeting()

//------------tuple
func refreshingWebPage() -> (code: Int, message: String) {
    return (200, "Success")
}

let (statusCode, message) = refreshingWebPage()
println("Recevied \(statusCode):\(message)")

let status = refreshingWebPage()
println("Recevied \(status.code):\(status.message)")


//-------------closure
let greetingPrinter = {
    println("HelloWorld!")
}
greetingPrinter()

func greetingPrinter2() -> () {
    println("HelloWorld!")
}
// task (closure)
func repeat(count: Int, task:() -> ()) {
    for i in 0..<count {
        task()
    }
}

repeat(2, {
    println("hello")
})

//=========>
repeat(2) {
    println("hello")
}