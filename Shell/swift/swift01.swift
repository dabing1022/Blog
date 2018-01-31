#!/usr/bin/swift

import Foundation

print("Hello World, Swift")


let fileMgr = FileManager.default
let path = fileMgr.currentDirectoryPath

print(path)

/***
 * 执行shell命令，返回执行结果和输出结果
 */
@discardableResult func shell(_ args: String...) -> (Int32, String) {
    let process = Process()
    process.launchPath = "/usr/bin/env"
    process.arguments = args
    
    let pipe = Pipe()
    process.standardOutput = pipe
    
    process.launch()
    process.waitUntilExit()
    
    
    let data = pipe.fileHandleForReading.readDataToEndOfFile()
    let output: String = String(data: data, encoding: .utf8)!
    
    return (process.terminationStatus, output)
}

//执行ls
var result = shell("ls").1
print("ls result:\n" + result)

enum ColorCode : String {
    case black = "\u{001B}[0;30m"
    case red = "\u{001B}[0;31m"
    case green = "\u{001B}[0;32m"
    case yellow = "\u{001B}[1;33m" //粗体
    case blue = "\u{001B}[5;34m" //粗体
    case magenta = "\u{001B}[0;35m"
    case cyan = "\u{001B}[0;36m"
    case white = "\u{001B}[0;37m"
    case `default` = "\u{001B}[0;39m"

}

func + (left: ColorCode, right: String) -> String {
    return left.rawValue + right
}

func + (left: String, right: ColorCode) -> String {
    return left + right.rawValue
}

result = shell("pwd").1
print("ls result" + ColorCode.blue + "\n" + result)
