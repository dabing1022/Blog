//: Playground - noun: a place where people can play

import UIKit

// function currying with Classes
class Car {
    var speed = 0
    
    func acceleratedBy(factor: Int) -> Int {
        speed += factor
        return speed
    }
}

let car1 = Car()
car1.acceleratedBy(10)
let a = Car.acceleratedBy(car1)
a(10)
a(10)