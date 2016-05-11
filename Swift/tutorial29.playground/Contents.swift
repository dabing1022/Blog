//: Playground - noun: a place where people can play

import UIKit

var string: NSString = "\n\ntest test 111\n\n"

// remove new line character set
string = string.stringByTrimmingCharactersInSet(NSCharacterSet.newlineCharacterSet())
string


string = "test test 222\n"
let lastLineBreakExsits = string.substringWithRange(NSRange(location: string.length - 1, length: 1)) == "\n"
if (lastLineBreakExsits) {
    string = string.stringByReplacingCharactersInRange(NSRange(location: string.length - 1, length: 1), withString: "")
}
    
