// Playground - noun: a place where people can play

import Cocoa

var str = "Hello, playground"

println("Hello World!")

var a = 42
a = 50
let myConstant = 51

let b1 = 70
let b2 = 70.0
let b3: Double = 70

let label = "ChildhoodAndy"
let age = 27
let infoLabel = label + String(age)

let apples = 5
let oranges = 6
let appleSummary = "I have \(apples) apples"
let orangeSummary = "I have \(oranges) oranges"

var shoppingList = ["11", "22", "33"]
shoppingList[1] = "44"

var nameInfo = [
    "andy":12,
    "lily":15,
]

nameInfo["frank"] = 18

let individualScores = [75, 43, 103, 87, 12]
var teamScore = 0
for score in individualScores {
    if score > 50 {
        teamScore += 3
    } else {
        teamScore += 1
    }
}
teamScore

var optionString: String? = "Hello"
optionString == nil

var optionName:String? = "Beijing"
optionName = nil
var greeting = "Hello"
if let name = optionName {
    greeting = "Hello, \(name)"
}


// while
var n = 2
while n < 100 {
    n = n * 2
}
n

//class
class Shape {
    var numberOfSides = 0
    func simpleDescription() -> String {
        return "A shape with \(numberOfSides) sides."
    }
}

var shape = Shape()
shape.numberOfSides = 100
shape.simpleDescription()

class NamedShape {
    var numberOfSides: Int = 0
    var name: String
    
    init(name: String) {
        self.name = name
    }
    
    func simpleDescription() -> String {
        return "A shape with \(numberOfSides) sides."
    }
}

var shape2 = NamedShape(name: "swift")
shape2.numberOfSides = 10
shape2.name = "swift2"
shape2.simpleDescription()
shape2.name


//-------------------------------------------------------------
enum Rank: Int {
    case Ace = 1
    case Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten
    case Jack, Queen, King
    func simpleDescription() -> String {
        switch self {
        case .Ace:
            return "ace"
        case .Jack:
            return "jack"
        case .Queen:
            return "queen"
        case .King:
            return "king"
        default:
            return String(self.toRaw())
        }
    }
}
let ace = Rank.Ace
let aceRawValue = ace.toRaw()


if let convertedRank = Rank.fromRaw(3) {
    let threeDescription = convertedRank.simpleDescription()
}

//-------------------------------------------------------------
enum Suit {
    case Spades, Hearts, Diamonds, Clubs
    func simpleDescription() -> String {
        switch self {
        case .Spades:
            return "spades"
        case .Hearts:
            return "hearts"
        case .Diamonds:
            return "diamonds"
        case .Clubs:
            return "clubs"
        }
    }
    
}
let hearts = Suit.Hearts
let heartsDescription = hearts.simpleDescription()
