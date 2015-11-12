//: Playground - noun: a place where people can play

import UIKit

public struct TestStruct {
    public var testParameter: Double
    public init(parameter: Double) {
        self.testParameter = parameter
    }
}

var test: TestStruct = TestStruct(parameter: 100.0)

// var test: TestStruct = 100  wrong...


//--------------------------
extension TestStruct: FloatLiteralConvertible {
    public init(floatLiteral value: FloatLiteralType) {
        self.init(parameter: value)
    }
}

var test2: TestStruct = 200.0


// 乘法和除法运算符优先级为150
infix operator ^^{ associativity right precedence 155 }
func ^^(lhs: Int, rhs: Int) -> Int {
    let l = Double(lhs)
    let r = Double(rhs)
    let p = pow(l, r)
    return Int(p)
}

(5 + 5) * 5 ^^ 2 // 10 * 25 = 250

enum KittenError: ErrorType {
    case NoKitten
}

func showKitten(kitten: String?) throws {
    guard let k = kitten else {
        print("There is no kitten")
        throw KittenError.NoKitten
//        return
//        fatalError()
    }
    
    print(k)
}

try showKitten(nil)