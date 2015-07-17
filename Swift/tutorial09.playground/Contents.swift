//: Playground - noun: a place where people can play

import UIKit

class MyClass {
    let oneYearInSecond: NSTimeInterval = 365 * 24 * 60 * 60
    var date: NSDate {
        willSet {
            print("即将将日期从 \(date) 设定至 \(newValue)")
        }
        didSet {
            if (date.timeIntervalSinceNow > oneYearInSecond) {
                print("设定的时间太晚了！")
                date = NSDate().dateByAddingTimeInterval(oneYearInSecond)
            }
            print("已经将日期从 \(oldValue) 设定至 \(date)")
        }
    }
    
    init() {
        date = NSDate()
    }
}

let foo = MyClass()
foo.date = foo.date.dateByAddingTimeInterval(10086)

class ClassA { }
class ClassB: ClassA { }

let obj: AnyObject = ClassB()

if (obj is ClassA) {
    print("属于 ClassA")
}

if (obj is ClassB) {
    print("属于 ClassB")
}

//---------------------
class A {
    class func method() {
        print("Hello")
    }
}

let typeA: A.Type = A.self
typeA.method()

// 或者
let anyClass: AnyClass = A.self
(anyClass as! A.Type).method()