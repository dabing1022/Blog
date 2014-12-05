// Playground - noun: a place where people can play
// http://www.raywenderlich.com/82599/swift-functional-programming-tutorial

// Simple Array Filtering

// Filtering the Old Way
var evens = [Int]()
for i in 1...10 {
    if i % 2 == 0 {
        evens.append(i)
    }
}
println(evens)

// Functional Filtering
func isEven(number: Int) -> Bool {
    return number % 2 == 0
}
evens = Array(1...10).filter(isEven)
println(evens)

evens = Array(1...10).filter { (number) in number % 2 == 0 }
println(evens)