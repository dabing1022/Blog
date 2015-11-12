//: Playground - noun: a place where people can play

import UIKit

var info: NSString = "200+车主与您顺路"

do {
    let carNoRegex = try NSRegularExpression(pattern: "[0-9]+[+]?", options: NSRegularExpressionOptions.CaseInsensitive)
    if let firstMatch = carNoRegex.firstMatchInString(info as String, options: NSMatchingOptions.ReportCompletion, range: NSMakeRange(0, info.length)) {
        let range = firstMatch.rangeAtIndex(0)
        let result = info.substringWithRange(range)
        let driversNumber = Int(result.componentsSeparatedByString("+")[0])
    }
    
} catch let error as NSError {
}

var thing = "car"
//let closure = {
//    [thing] in
//    return "I love \(thing)"   //output: I love car
//}
let closure = {
    return "I love \(thing)"   //output: I love airplane
}

thing = "airplane"
closure()



// divide
// 01 way
func divide(dividened: Double?, by divisor: Double?) -> Double? {
    if dividened == .None {
        return .None
    }
    
    if divisor == .None {
        return .None
    }
    
    if divisor == 0 {
        return .None
    }
    
    return dividened! / divisor!;
}

func divide2(dividened: Double?, by divisor: Double?) -> Double? {
    guard let dividened = dividened else { return .None }
    guard let divisor = divisor else { return .None }
    guard divisor != 0 else { return .None }
    return dividened / divisor
}

func divide3(dividened: Double?, by divisor: Double?) -> Double? {
    guard let dividened = dividened, divisor = divisor where divisor != 0 else { return .None }
    return dividened / divisor
}

divide(10, by: 10)
divide2(20, by: 2)
divide3(30, by: 3)