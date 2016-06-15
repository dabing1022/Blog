import Foundation

// Create a Task instance
let task = NSTask()

// Set the task parameters
task.launchPath = "/usr/bin/env"
task.arguments = ["ls", "-la"]

// Create a Pipe and make the task
// put all the output there
let pipe = NSPipe()
task.standardOutput = pipe

let outputHandle = pipe.fileHandleForReading
outputHandle.waitForDataInBackgroundAndNotify()

// When new data is available
var dataAvailable : NSObjectProtocol!
dataAvailable = NSNotificationCenter.defaultCenter().addObserverForName(NSFileHandleDataAvailableNotification,
    object: outputHandle, queue: nil) {  notification -> Void in
        let data = pipe.fileHandleForReading.availableData
        if data.length > 0 {
            if let str = NSString(data: data, encoding: NSUTF8StringEncoding) {
                print("Task sent some data: \(str)")
            }
            outputHandle.waitForDataInBackgroundAndNotify()
        } else {
            NSNotificationCenter.defaultCenter().removeObserver(dataAvailable)
        }
}

// When task has finished
var dataReady : NSObjectProtocol!
dataReady = NSNotificationCenter.defaultCenter().addObserverForName(NSTaskDidTerminateNotification,
    object: pipe.fileHandleForReading, queue: nil) { notification -> Void in
        print("Task terminated!")
        NSNotificationCenter.defaultCenter().removeObserver(dataReady)
}

// Launch the task
task.launch()
