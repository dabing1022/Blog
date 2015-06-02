//: Playground - noun: a place where people can play

import UIKit
import Foundation

var str = "Hello, playground"

let data = str.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: true)

let bytesArray = data?.bytes
