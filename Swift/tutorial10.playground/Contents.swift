//: Playground - noun: a place where people can play

import UIKit

let font = UIFont.systemFontOfSize(14)
let testStr: NSString = "This is a test string"
let options: NSStringDrawingOptions = unsafeBitCast(NSStringDrawingOptions.TruncatesLastVisibleLine.rawValue | NSStringDrawingOptions.UsesFontLeading.rawValue | NSStringDrawingOptions.UsesLineFragmentOrigin.rawValue, NSStringDrawingOptions.self)

let size1 = testStr.boundingRectWithSize(CGSize(width: 1000, height: 1000), options: NSStringDrawingOptions.TruncatesLastVisibleLine, attributes: [NSFontAttributeName: font], context: nil)
let size2 = testStr.boundingRectWithSize(CGSize(width: 1000, height: 1000), options: NSStringDrawingOptions.UsesLineFragmentOrigin, attributes: [NSFontAttributeName: font], context: nil)
let size3 = testStr.boundingRectWithSize(CGSize(width: 1000, height: 1000), options: NSStringDrawingOptions.UsesFontLeading, attributes: [NSFontAttributeName: font], context: nil)
let size4 = testStr.boundingRectWithSize(CGSize(width: 1000, height: 1000), options: options, attributes: [NSFontAttributeName: font], context: nil)








