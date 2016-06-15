import Foundation

// Create a Task instance
let task = NSTask()

// Set the task parameters
task.launchPath = "/usr/bin/env"
task.arguments = ["pwd"]

// Create a Pipe and make the task
// put all the output there
let pipe = NSPipe()
task.standardOutput = pipe

// Launch the task
task.launch()

// Get the data
let data = pipe.fileHandleForReading.readDataToEndOfFile()
let output = NSString(data: data, encoding: NSUTF8StringEncoding)

print(output!)
