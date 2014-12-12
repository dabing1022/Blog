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


// control flow

//如果你不需要知道区间内每一项的值，你可以使用下划线（_）替代变量名来忽略对值的访问
//下划线符号_（替代循环中的变量）能够忽略具体的值，并且不提供循环遍历时对值的访问
let base = 3
let power = 10
var answer = 1
for _ in 1...power {
    answer *= base
}
println("\(base) to the power of \(power) is \(answer)")

//使用for-in遍历一个数组所有元素
let names = ["Anna", "Alex", "Brian", "Jack"]
for name in names {
    println("Hello, \(name)!")
}

//遍历一个字典来访问它的键值对（key-value pairs）
let numberOfLegs = ["spider":8, "ant":6, "cat":4]
for (animalName, legCount) in numberOfLegs {
    println("\(animalName)s have \(legCount) legs")
}

//除了数组和字典，你也可以使用for-in循环来遍历字符串中的字符（Character）
for character in "Hello" {
    println(character)
}

//Game:Chutes and Ladders
let finalSquare = 25
var board = [Int](count: finalSquare + 1, repeatedValue: 0)
board[03] = +08; board[06] = +11; board[09] = +09; board[10] = +02
board[14] = -10; board[19] = -11; board[22] = -02; board[24] = -08

var square = 0
var diceRoll = 0
/* while
while square < finalSquare {
    if ++diceRoll == 7 { diceRoll = 1 }
    
    square += diceRoll
    diceRoll
    if square < board.count {
        square += board[square]
    }
}
*/

/* do-while
do {
    // 顺着梯子爬上去或者顺着蛇滑下去
    square += board[square]
    // 掷骰子
    if ++diceRoll == 7 { diceRoll = 1 }
    // 根据点数移动
    square += diceRoll
} while square < finalSquare
println("Game over!")
*/

// switch while break continue
gameLoop: while square != finalSquare {
    if ++diceRoll == 7 { diceRoll = 1 }
    switch square + diceRoll {
    case finalSquare:
        // 到达最后一个方块，游戏结束
        break gameLoop
    case let newSquare where newSquare > finalSquare:
        // 超出最后一个方块，再掷一次骰子
        continue gameLoop
    default:
        // 本次移动有效
        square += diceRoll
        square += board[square]
    }
}
println("Game over!")

//switch 
let anotherCharacter: Character = "a"
switch anotherCharacter {
case "a":
    println("The letter a")
case "A":
    println("The letter A")
default:
    println("Not the letter A")
}

//区间匹配（Range Matching）
let count = 3_000_000_000_000
let countedThings = "stars in the Milky Way"
var naturalCount: String
switch count {
case 0:
    naturalCount = "no"
case 1...3:
    naturalCount = "a few"
case 4...9:
    naturalCount = "several"
case 10...99:
    naturalCount = "tens of"
case 100...999:
    naturalCount = "hundreds of"
case 1000...999_999:
    naturalCount = "thousands of"
default:
    naturalCount = "millions and millions of"
}
println("There are \(naturalCount) \(countedThings).")

//Tuple
let somePoint = (0, 1)
switch somePoint {
case (0, 0):
    println("(0, 0) is at the origin")
case (_, 0):
    println("(\(somePoint.0), 0) is on the x-axis")
case (0, _):
    println("(0, \(somePoint.1)) is on the y-axis")
case (-2...2, -2...2):
    println("(\(somePoint.0), \(somePoint.1)) is inside the box")
default:
    println("(\(somePoint.0), \(somePoint.1)) is outside the box")
}


//值绑定
//声明了常量x和y的占位符，用于临时获取元组anotherPoint的一个或两个值
let anotherPoint = (2, 0)
switch anotherPoint {
case (let x, 0):
    println("on the x-axis with an x value of \(x)")
case (0, let y):
    println("on the y-axis with an y value of \(y)")
case let (x, y):
    println("somewhere else at (\(x), \(y))")
}


//case 分支的模式可以使用where语句来判断额外的条件
let yetAnotherPoint = (1, -1)
switch yetAnotherPoint {
case let (x, y) where x == y:
    println("(\(x), \(y)) is on the line x == y")
case let (x, y) where x == -y:
    println("(\(x), \(y)) is on the line x == -y")
case let (x, y):
    println("(\(x), \(y)) is just some arbitrary point")
}