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