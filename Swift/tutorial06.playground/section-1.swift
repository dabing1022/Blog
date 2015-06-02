import Foundation

var priorityQ = Array<Int>()

priorityQ.append(100)
priorityQ.append(200)
priorityQ.append(300)
priorityQ.append(150)
priorityQ.append(250)

priorityQ.sort { (numberA: Int, numberB: Int) -> Bool in
    return numberA <= numberB
}


while (priorityQ.first > 50 && priorityQ.count > 0) {
    println("delete number \(priorityQ.first)")
    priorityQ.removeAtIndex(0)
}

priorityQ


class Class1 : NSObject {
    
}
    


class Class2 : Class1 {
    
}

class Class3: Class1 {
    
}

var v1 = Class1()
var v2 = Class2()
var v3 = Class3()

v1.isKindOfClass(Class1)
v2.isKindOfClass(Class2)
v2.isKindOfClass(Class1)
v2.isKindOfClass(v3.classForCoder)
