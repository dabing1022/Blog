/*: # [Functional programming](http://www.raywenderlich.com/82599/swift-functional-programming-tutorial) */

/*: 
# What is Functional Programming?

Briefly put, functional programming is a programming paradigm that emphasizes calculations via mathematical-style functions, immutability and expressiveness, and minimizes the use of variables and state.

Since there’s minimal shared state and each function is like an island in the ocean of your app, it makes things easier to test. Functional programming has also come into popularity because it can make concurrency and parallel processing easier to work with. That’s one more thing in your toolbox to improve performance in these days of multi-core devices.

Its time to put the fun- into functional programming!
*/

/*:
# 01 Simple Array Filtering
*/

/*:
## Filtering the Old Way
*/

import Foundation

var evens = [Int]()
for i in 1...10 {
    if i % 2 == 0 {
        evens.append(i)
    }
}
evens

/*:
## Functional Filtering
*/

func isEven(number: Int) -> Bool {
    return number % 2 == 0
}
evens = Array(1...10).filter(isEven)
evens = Array(1...10).filter {
    $0 % 2 == 0
}

/*:
## The Magic Behide Filter
*/

func myFilter<T>(source: [T], predicate:(T) -> Bool) -> [T] {
    var result = [T]()
    for i in source {
        if predicate(i) {
            result.append(i)
        }
    }
    
    return result
}
evens = myFilter(Array(1...10), predicate:{ $0 % 2 == 0 })

/*:
# 02 Reducing
*/

/*:
## Manual reduction
*/
evens = [Int]()
for i in 1...10 {
    if i % 2 == 0 {
        evens.append(i)
    }
}

var evenSum = 0
for i in evens {
    evenSum += i
}
evenSum

/*:
## Functional Reduce

func reduce<U>(initial: U, combine: (U, T) -> U) -> U

*/
evenSum = Array(1...10)
    .filter { $0 % 2 == 0 }
    .reduce(0) { (total, number) in total + number }

let maxNumber = Array(1...10)
    .reduce(0) { (total, number) in max(total, number) }
let nubmersStr = Array(1...10)
    .reduce("numbers:") { (total, number) in total + " \(number)" }

/*:
## The Magic Behide Reduce
*/

extension Array {
    func myReduce<T, U>(seed: U, combiner:(U, T) -> U) -> U {
        var current = seed
        for item in self {
            current = combiner(current, item as! T)
        }
        
        return current
    }
}


/*:
# 03 Building an Index
*/

let words = ["Cat", "Chicken", "fish", "Dog", "Mouse", "Pig", "Monkey"]
typealias Entry = (Character, [String])
func buildIndex(words: [String]) -> [Entry] {
    var results = [Entry]()
    
    var letters = [Character]()
    for word in words {
        let firstLetter = Character(word.substringToIndex(word.startIndex.advancedBy(1)).uppercaseString)
        if !letters.contains(firstLetter) {
            letters.append(firstLetter)
        }
    }
    
    for letter in letters {
        var wordsForLetter = [String]()
        for word in words {
            let firstLetter = Character(word.substringToIndex(word.startIndex.advancedBy(1)).uppercaseString)
            
            if firstLetter == letter {
                wordsForLetter.append(word)
            }
        }
        
        results.append((letter, wordsForLetter))
    }
    
    return results
}
print(buildIndex(words))

/*:
## Build an Index the Functional Way
*/

func distinct<T: Equatable>(source: [T]) -> [T] {
    var unique = [T]()
    for item in source {
        if !unique.contains(item) {
            unique.append(item)
        }
    }
    
    return unique
}

func buildIndexInFunctionalWay(words: [String]) -> [Entry] {
    func firstLetter(str: String) -> Character {
        return Character(str.substringToIndex(str.startIndex.advancedBy(1)).uppercaseString)
    }
    let distinctLetters = distinct(words.map(firstLetter))
    return distinctLetters.map {
        (letter) -> Entry in
        return (letter, words.filter {
            (word) -> Bool in
            Character(word.substringToIndex(word.startIndex.advancedBy(1)).uppercaseString) == letter
            })
    }
}
print(buildIndexInFunctionalWay(words))
