// Playground - noun: a place where people can play

//class

class Vehicle {
    var numberOfWheels: Int = 0
    var someStoredProperty:String = ""
    
    //description 为 computed property
    //具备读写权限
    var description: String {
        get {
            return "\(numberOfWheels) wheels"
        }
    
        set {
            //newValue 为 swift 关键字
            someStoredProperty = newValue
        }
    }
}

var vehicle = Vehicle()
vehicle.description = "hahah"
vehicle.someStoredProperty
vehicle.numberOfWheels = 4
vehicle.description

class Bicycle: Vehicle {
    init() {
        super.init() //在swift中，不一定必须要调用父类的初始化方法
        numberOfWheels = 2
    }
}

let myBike = Bicycle()
myBike.description

// overriding a property

class Car : Vehicle {
    var speed: Double = 0.0
    init() {
        super.init()
        numberOfWheels = 4
    }
    
    override var description: String {
        get {
            return super.description + ", per hour \(speed) miles"
        }
        set {
            super.someStoredProperty = "car" + newValue
        }
    }
}

// Property Observers
// 监护车
class ParentsCar: Car {
    // 重写stored property
    override var speed: Double = 0.0 {
        willSet {
            if newValue > 65.0 {
                println("Be careful!!!")
            }
        }
        didSet {
            
        }
    }
}

// --------------------------------

class Counter {
    var count = 0
    func increment() {
        count++
    }
    
    func incrementBy(amout: Int) {
        count += amout
    }
    
    // 参数count和属性count同名，所以下面用self.count来表示类属性count
    func resetToCount(count: Int) {
        self.count = count
    }
}