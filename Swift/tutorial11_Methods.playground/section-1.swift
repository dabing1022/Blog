// Playground - noun: a place where people can play
// https://github.com/numbbbbb/the-swift-programming-language-in-chinese/blob/gh-pages/source/chapter2/11_Methods.md

class Counter {
    var count = 0
    func increment() {
        count++
        //self.count++
    }
    
    func incrementBy(amount: Int) {
        count += amount
    }
    
    func incrementBy2(amount: Int, numberOfTimes: Int) {
        count += amount * numberOfTimes
    }
    
    func incrementBy3(#amount: Int, numberOfTimes: Int) {
        count += amount * numberOfTimes
    }
    
    func reset() {
        count = 0
    }
}

let counter = Counter()
counter.increment()
counter.incrementBy(10)
counter.reset()
counter.incrementBy3(amount:10, numberOfTimes: 2)
counter.incrementBy2(10, numberOfTimes: 3)

// ------------------------------
struct Point {
    var x = 0.0, y = 0.0
    func isToTheRightOfX(x: Double) -> Bool {
        // 在这种情况下，参数名称享有优先权，并且在引用属性时必须使用一种更严格的方式。这时你可以使用self属性来区分参数名称和属性名称。
        return self.x > x
    }
    
    // mutating 在某个具体的方法中修改结构体或者枚举的属性
    mutating func moveByX(deltaX: Double, y deltaY: Double) {
        x += deltaX
        y += deltaY
        
        // self = Point(x: x + deltaX, y: y + deltaY)
    }
}
let somePoint = Point(x: 4.0, y: 5.0)
if somePoint.isToTheRightOfX(1.0) {
    println("This point is to the right of the line where x == 1.0")
}
var somePoint2 = Point(x: 1.0, y: 1.0)
somePoint2.moveByX(2.0, y: 3.0)
println("The point is now at (\(somePoint2.x), \(somePoint2.y))")