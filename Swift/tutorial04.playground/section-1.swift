// Playground - noun: a place where people can play

import Cocoa

// struct
struct Point {
    var x, y: Double
    
    mutating func moveToRightBy(dx: Double) {
        x += dx
    }
}

var point = Point(x: 0.0, y: 0.0)
point.moveToRightBy(100.0)
point

// enum
enum Day: Int {
    case Monday = 1
    case Tuesday = 2
    case Wednesday = 3
    case Thursday = 4
}

enum Day2: Int {
    case Monday = 1, Tuesday, Wednesday, Thursday
}

let day = Day2.Thursday
let dayNumber = Day2.Thursday.toRaw()

// ---------------------------------
enum IllegalCharactor: Character {
    case Tab = "\t"
    case Return = "\n"
    case Underline = "_"
    case Dot = "."
    case Whitespace = " "
}

// ---------------------------------
enum Direction {
    case North, South, East, West
}

var direction = Direction.South
direction = .West

// enum关联值，强大！
enum TrainStatus {
    case OnTime
    case Delayed(Int)
    
    // 初始化
    init() {
        self = OnTime
    }
    
    // computed property
    var description: String {
        switch self {
            case .OnTime:
                return "on time!"
            case .Delayed(let minutes):
                return "delayed \(minutes) minutes"
            default:
                return " "
        }
    }
    
}

var status = TrainStatus.OnTime
// or
// var status = TrainStatus()
status.description
status = .Delayed(100)
status.description
status = .Delayed(200)
status.description

// --------------Nested Types
class Train {
    enum TrainStatus {
        case OnTime
        case Delayed(Int)
        
        // 初始化
        init() {
            self = OnTime
        }
        
        // computed property
        var description: String {
        switch self {
        case .OnTime:
            return "on time!"
        case .Delayed(let minutes):
            return "delayed \(minutes) minutes"
        default:
            return " "
            }
        }
        
    }
}

// ---------------Extensions
// class struct (almost everything)
extension Size {
    // Don't forget the word "mutating"!
    mutating func increaseByFactor(factor:Int) {
//        width *= factor
//        height *= factor
    }
}

extension Int {
    func repettitions(task:() -> ()) {
        for i in 0..<self {
            task()
        }
    }
}

10.repettitions({
    println("Swift")
})


// -----------------Generic

struct IntStack {
    var elements = [Int]()
    
    mutating func push(element: Int) {
        elements.append(element)
    }
    
    mutating func pop() -> Int {
        return elements.removeLast()
    }
}


struct Stack<T> {
    var elements = [T]()
    
    mutating func push(element: T) {
        elements.append(element)
    }
    
    mutating func pop() -> T {
        return elements.removeLast()
    }
}


var intStack = Stack<Int>()
intStack.push(50)
intStack.pop()

let names = ["Chris", "Alex", "Ewa", "Barry", "Daniella"]
func backwards(s1: String, s2: String) -> Bool {
    return s1 > s2
}
var reversed = sorted(names, backwards)
