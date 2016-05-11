//: Playground - noun: a place where people can play

class Class2 {
    func g() {
        print("Class2  g()")
    }
}

class Class1 {
    func f() {
        let obj2 = Class2()
        obj2.g()
    }
}

let obj1 = Class1()
obj1.f()


class Class3 {
    func test1() {
        test2()
        test3()
        test4()
    }

    func test2() { }
    func test3() { }
    func test4() { }
}

class Class4 {
    func fibonacci(number: Int) -> Int {
        if (number == 1 || number == 0) {
            return number
        } else {
            return fibonacci(number - 1) + fibonacci(number - 2)
        }
    }
}

let obj4 = Class4()
obj4.fibonacci(10)

//----------------

class Class5 {
    func test5() {
        let obj1 = Class1()
        obj1.f()
    }

    func rock() {
        print("Let's rock!")
    }
}

class Class6 {
    var obj5 = Class5()
    func rock() {
        obj5.rock()
    }
}
let obj6 = Class6()
obj6.rock()
=======
protocol Hello {
    func sayHello()
}

// protocol extension
extension Hello {
    func sayHello() {
        print("hello")
    }
}

struct Person: ArrayLiteralConvertible, Hello {
    var name: String = ""
    var id: String = ""
    typealias Element = String
    init(arrayLiteral elements: String...) {
        if elements.count == 2 {
            name = elements[0]
            id = elements[1]
        }
    }
}

let person1: Person = ["Tommy", "20"]
person1.name
person1.id
person1.sayHello()

let person2: Person = ["Jerry", "15"]
person2.name
person2.id

let person3: Person = ["Jerry", "20"]
person3.name
person3.id

extension Person: Equatable {}
func == (p1: Person, p2: Person) -> Bool {
    return p1.id == p2.id
}


person1 == person2
person1 == person3
