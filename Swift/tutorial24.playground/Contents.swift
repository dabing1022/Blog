//: Playground - noun: a place where people can play

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

