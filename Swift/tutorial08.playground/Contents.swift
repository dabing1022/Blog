//: Playground - noun: a place where people can play

import UIKit
import Foundation

class Test {
    func blockOperationsTest1(){
        print("blockOperationTest1")
        let operationQueue = NSOperationQueue()
        
        let operation1 : NSBlockOperation = NSBlockOperation (block: {
            self.doCalculations()
            
            let operation2 : NSBlockOperation = NSBlockOperation (block: {
                
                self.doSomeMoreCalculations()
                
            })
            operationQueue.addOperation(operation2)
        })
        operationQueue.addOperation(operation1)
    }
    
    func doCalculations(){
        NSLog("do Calculations")
        for i in 100...105{
            print("i in do calculations is \(i)", appendNewline: true)
            sleep(1)
        }
    }
    
    func doSomeMoreCalculations(){
        NSLog("do Some More Calculations")
        for j in 1...5{
            print("j in do some more calculations is \(j)", appendNewline: true)
            sleep(1)
        }
        
    }
}

let test = Test()
test.blockOperationsTest1()
