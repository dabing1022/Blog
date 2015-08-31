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


